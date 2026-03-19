package test.com.projectlibre1.dialog.calendar;

import java.awt.GraphicsEnvironment;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.projectlibre1.dialog.calendar.CalendarView;
import com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogBox;
import com.projectlibre1.pm.calendar.WorkingCalendar;
import com.projectlibre1.pm.graphic.frames.GraphicManager;
import com.projectlibre1.pm.task.Project;
import com.projectlibre1.undo.DataFactoryUndoController;

final class CalendarDialogAuditSupport
{
	interface ThrowingRunnable
	{
		void run() throws Exception;
	}

	static final class DialogFixture
	{
		private final JFrame frame;
		private final ChangeWorkingTimeDialogBox dialog;
		private final long openNanos;

		DialogFixture(final JFrame _frame, final ChangeWorkingTimeDialogBox _dialog, final long _openNanos)
		{
			frame = _frame;
			dialog = _dialog;
			openNanos = _openNanos;
		}

		JFrame getFrame()
		{
			return frame;
		}

		ChangeWorkingTimeDialogBox getDialog()
		{
			return dialog;
		}

		long getOpenNanos()
		{
			return openNanos;
		}

		CalendarView getCalendarView() throws Exception
		{
			return (CalendarView) getField(dialog, "sdCalendar");
		}

		JList getCalendarList() throws Exception
		{
			return (JList) getField(dialog, "calendarList");
		}

		JList getWeekDayList() throws Exception
		{
			return (JList) getField(dialog, "weekDayList");
		}

		JList getExceptionList() throws Exception
		{
			return (JList) getField(dialog, "exceptionList");
		}

		JRadioButton getWorkingButton() throws Exception
		{
			return (JRadioButton) getField(dialog, "working");
		}

		JRadioButton getNonWorkingButton() throws Exception
		{
			return (JRadioButton) getField(dialog, "nonWorking");
		}

		JTextField[] getTimeStartFields() throws Exception
		{
			return (JTextField[]) getField(dialog, "timeStart");
		}

		JTextField[] getTimeEndFields() throws Exception
		{
			return (JTextField[]) getField(dialog, "timeEnd");
		}

	}

	static final class TimingProfile
	{
		private final Map<String, List<Long>> samples = new LinkedHashMap<String, List<Long>>();

		void addSample(final String name, final long durationNanos)
		{
			List<Long> durations = (List<Long>) samples.get(name);
			if (durations == null)
			{
				durations = new ArrayList<Long>();
				samples.put(name, durations);
			}
			durations.add(Long.valueOf(durationNanos));
		}

		TimingSummary summarize(final String name)
		{
			List<Long> durations = (List<Long>) samples.get(name);
			if (durations == null || durations.isEmpty())
			{
				return new TimingSummary(name, 0, 0.0d, 0.0d);
			}
			List<Long> sorted = new ArrayList<Long>(durations);
			Collections.sort(sorted);
			int p95Index = (int) Math.ceil(sorted.size() * 0.95d) - 1;
			if (p95Index < 0)
			{
				p95Index = 0;
			}
			long maxNanos = ((Long) sorted.get(sorted.size() - 1)).longValue();
			long p95Nanos = ((Long) sorted.get(p95Index)).longValue();
			return new TimingSummary(name, sorted.size(), nanosToMillis(p95Nanos), nanosToMillis(maxNanos));
		}

		String format()
		{
			StringBuffer buffer = new StringBuffer();
			for (Map.Entry<String, List<Long>> entry : samples.entrySet())
			{
				TimingSummary summary = summarize(entry.getKey());
				if (buffer.length() > 0)
				{
					buffer.append(System.getProperty("line.separator"));
				}
				buffer.append(summary.toString());
			}
			return buffer.toString();
		}
	}

	static final class TimingSummary
	{
		private final String name;
		private final int sampleCount;
		private final double p95Millis;
		private final double maxMillis;

		TimingSummary(final String _name, final int _sampleCount, final double _p95Millis, final double _maxMillis)
		{
			name = _name;
			sampleCount = _sampleCount;
			p95Millis = _p95Millis;
			maxMillis = _maxMillis;
		}

		double getP95Millis()
		{
			return p95Millis;
		}

		double getMaxMillis()
		{
			return maxMillis;
		}

		public String toString()
		{
			return name + ": samples=" + sampleCount + ", p95=" + round(p95Millis) + " ms, max=" + round(maxMillis) + " ms";
		}
	}

	private CalendarDialogAuditSupport()
	{
	}

	static boolean isUiTestSkipped()
	{
		return GraphicsEnvironment.isHeadless();
	}

	static DialogFixture openDialogFixture() throws Exception
	{
		return (DialogFixture) onEdt(new Callable<DialogFixture>()
		{
			public DialogFixture call() throws Exception
			{
				JFrame frame = new JFrame("CalendarAuditHost");
				new GraphicManager(frame);
				Project project = createFreshProject();
				WorkingCalendar calendar = (WorkingCalendar) project.getWorkCalendar();
				ChangeWorkingTimeDialogBox dialog = ChangeWorkingTimeDialogBox.getInstance(frame, project, calendar, null, false,
						project.getUndoController());
				((DataFactoryUndoController) getField(dialog, "undoController")).setDataFactory(project);
				dialog.setModal(false);

				long start = System.nanoTime();
				JComponent content = dialog.createContentPanel();
				dialog.getContentPane().setLayout(new BorderLayout());
				dialog.getContentPane().add(content, BorderLayout.CENTER);
				dialog.setSize(content.getPreferredSize());
				dialog.validate();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
				long end = System.nanoTime();

				return new DialogFixture(frame, dialog, end - start);
			}
		});
	}

	static void disposeFixture(final DialogFixture fixture) throws Exception
	{
		if (fixture == null)
		{
			return;
		}
		onEdt(new ThrowingRunnable()
		{
			public void run()
			{
				if (fixture.getDialog().isDisplayable())
				{
					fixture.getDialog().setVisible(false);
					fixture.getDialog().dispose();
				}
				if (fixture.getFrame().isDisplayable())
				{
					fixture.getFrame().dispose();
				}
			}
		});
	}

	static void closeViaCancel(final DialogFixture fixture) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run() throws Exception
			{
				invokeMethod(fixture.getDialog(), "onCancel");
			}
		});
	}

	static void closeViaOk(final DialogFixture fixture) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run()
			{
				fixture.getDialog().onOk();
			}
		});
	}

	static void closeViaWindow(final DialogFixture fixture) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run()
			{
				fixture.getDialog().dispatchEvent(new WindowEvent(fixture.getDialog(), WindowEvent.WINDOW_CLOSING));
			}
		});
	}

	static boolean isShowing(final DialogFixture fixture) throws Exception
	{
		return ((Boolean) onEdt(new Callable<Boolean>()
		{
			public Boolean call()
			{
				return Boolean.valueOf(fixture.getDialog().isShowing());
			}
		})).booleanValue();
	}

	static void moveDisplayedMonth(final DialogFixture fixture, final int monthDelta) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run() throws Exception
			{
				java.util.Calendar calendar = java.util.Calendar.getInstance();
				calendar.setTimeInMillis(fixture.getCalendarView().getFirstDisplayedDate());
				calendar.add(java.util.Calendar.MONTH, monthDelta);
				fixture.getCalendarView().setFirstDisplayedDate(calendar.getTimeInMillis());
			}
		});
	}

	static void selectWeekDay(final DialogFixture fixture, final int index) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run() throws Exception
			{
				fixture.getWeekDayList().setSelectedIndex(index);
			}
		});
	}

	static void selectCalendar(final DialogFixture fixture, final int index) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run() throws Exception
			{
				fixture.getCalendarList().setSelectedIndex(index);
			}
		});
	}

	static int getCalendarCount(final DialogFixture fixture) throws Exception
	{
		return ((Integer) onEdt(new Callable<Integer>()
		{
			public Integer call() throws Exception
			{
				return Integer.valueOf(fixture.getCalendarList().getModel().getSize());
			}
		})).intValue();
	}

	static int getExceptionCount(final DialogFixture fixture) throws Exception
	{
		return ((Integer) onEdt(new Callable<Integer>()
		{
			public Integer call() throws Exception
			{
				return Integer.valueOf(fixture.getExceptionList().getModel().getSize());
			}
		})).intValue();
	}

	static void createSingleDayException(final DialogFixture fixture) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run() throws Exception
			{
				CalendarView calendarView = fixture.getCalendarView();
				long date = calendarView.getFirstDisplayedDate();
				calendarView.selectSingleDate(date);
				fixture.getNonWorkingButton().doClick();
			}
		});
	}

	static void selectException(final DialogFixture fixture, final int index) throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run() throws Exception
			{
				fixture.getExceptionList().setSelectedIndex(index);
			}
		});
	}

	static void editWorkingHoursAndTriggerSave(final DialogFixture fixture, final int sourceWeekdayIndex, final int targetWeekdayIndex)
			throws Exception
	{
		onEdt(new ThrowingRunnable()
		{
			public void run() throws Exception
			{
				fixture.getWeekDayList().setSelectedIndex(sourceWeekdayIndex);
				fixture.getWorkingButton().doClick();

				JTextField[] timeStart = fixture.getTimeStartFields();
				JTextField[] timeEnd = fixture.getTimeEndFields();
				timeStart[0].setText("8:00");
				timeEnd[0].setText("12:00");
				timeStart[1].setText("13:00");
				timeEnd[1].setText("17:00");
				for (int i = 2; i < timeStart.length; i++)
				{
					timeStart[i].setText("");
					timeEnd[i].setText("");
				}

				fixture.getWeekDayList().setSelectedIndex(targetWeekdayIndex);
			}
		});
	}

	static boolean isDirtyWorkingHours(final DialogFixture fixture) throws Exception
	{
		return ((Boolean) onEdt(new Callable<Boolean>()
		{
			public Boolean call() throws Exception
			{
				return (Boolean) getField(fixture.getDialog(), "dirtyWorkingHours");
			}
		})).booleanValue();
	}

	static boolean isUnsaved(final DialogFixture fixture) throws Exception
	{
		return ((Boolean) onEdt(new Callable<Boolean>()
		{
			public Boolean call() throws Exception
			{
				return (Boolean) getField(fixture.getDialog(), "unsaved");
			}
		})).booleanValue();
	}

	static int countDisplayableCalendarDialogs()
	{
		int count = 0;
		Window[] windows = Window.getWindows();
		for (int i = 0; i < windows.length; i++)
		{
			if (windows[i] instanceof ChangeWorkingTimeDialogBox && windows[i].isDisplayable())
			{
				count++;
			}
		}
		return count;
	}

	static long measureNanos(final ThrowingRunnable runnable) throws Exception
	{
		long start = System.nanoTime();
		onEdt(runnable);
		return System.nanoTime() - start;
	}

	static <T> T onEdt(final Callable<T> callable) throws Exception
	{
		if (SwingUtilities.isEventDispatchThread())
		{
			return callable.call();
		}

		final Object[] result = new Object[1];
		final Exception[] failure = new Exception[1];
		SwingUtilities.invokeAndWait(new Runnable()
		{
			public void run()
			{
				try
				{
					result[0] = callable.call();
				}
				catch (Exception e)
				{
					failure[0] = e;
				}
			}
		});
		if (failure[0] != null)
		{
			throw failure[0];
		}
		return (T) result[0];
	}

	static void onEdt(final ThrowingRunnable runnable) throws Exception
	{
		onEdt(new Callable<Object>()
		{
			public Object call() throws Exception
			{
				runnable.run();
				return null;
			}
		});
	}

	static Object getField(final Object target, final String fieldName) throws Exception
	{
		Field field = findField(target.getClass(), fieldName);
		return field.get(target);
	}

	static void setField(final Object target, final String fieldName, final Object value) throws Exception
	{
		Field field = findField(target.getClass(), fieldName);
		field.set(target, value);
	}

	static void assertWithinBudget(final junit.framework.TestCase testCase, final String label, final TimingSummary summary,
			final double maxMillisBudget)
	{
		testCase.assertTrue(label + " max " + round(summary.getMaxMillis()) + " ms exceeded budget " + round(maxMillisBudget) + " ms",
				summary.getMaxMillis() <= maxMillisBudget);
	}

	static double nanosToMillis(final long nanos)
	{
		return nanos / 1000000.0d;
	}

	static double round(final double value)
	{
		return Math.round(value * 100.0d) / 100.0d;
	}

	private static Project createFreshProject() throws Exception
	{
		Constructor<Project> constructor = Project.class.getDeclaredConstructor(new Class[] { boolean.class });
		constructor.setAccessible(true);
		Project project = constructor.newInstance(new Object[] { Boolean.TRUE });
		project.setUndoController(new DataFactoryUndoController(project));
		return project;
	}

	private static Field findField(final Class<?> type, final String fieldName) throws Exception
	{
		Class<?> current = type;
		while (current != null)
		{
			try
			{
				Field field = current.getDeclaredField(fieldName);
				field.setAccessible(true);
				return field;
			}
			catch (NoSuchFieldException e)
			{
				current = current.getSuperclass();
			}
		}
		throw new NoSuchFieldException(fieldName);
	}

	private static Object invokeMethod(final Object target, final String methodName) throws Exception
	{
		Class<?> current = target.getClass();
		while (current != null)
		{
			try
			{
				Method method = current.getDeclaredMethod(methodName, new Class[0]);
				method.setAccessible(true);
				return method.invoke(target, new Object[0]);
			}
			catch (NoSuchMethodException e)
			{
				current = current.getSuperclass();
			}
		}
		throw new NoSuchMethodException(methodName);
	}
}
