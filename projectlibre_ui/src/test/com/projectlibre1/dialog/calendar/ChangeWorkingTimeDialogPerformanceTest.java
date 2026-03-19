package test.com.projectlibre1.dialog.calendar;

import junit.framework.TestCase;

public class ChangeWorkingTimeDialogPerformanceTest extends TestCase
{
	private static final double MAX_INTERACTION_MILLIS = Double.parseDouble(
			System.getProperty("calendar.audit.maxInteractionMillis", "250"));
	private static final double MAX_CLOSE_MILLIS = Double.parseDouble(System.getProperty("calendar.audit.maxCloseMillis", "500"));
	private static final double MAX_OPEN_MILLIS = Double.parseDouble(System.getProperty("calendar.audit.maxOpenMillis", "750"));

	public void testEdtInteractionBudget() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		warmUpDialog();

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		CalendarDialogAuditSupport.TimingProfile profile = new CalendarDialogAuditSupport.TimingProfile();
		profile.addSample("open", fixture.getOpenNanos());

		try
		{
			for (int i = 0; i < 30; i++)
			{
				profile.addSample("monthForward",
						CalendarDialogAuditSupport.measureNanos(new CalendarDialogAuditSupport.ThrowingRunnable()
						{
							public void run() throws Exception
							{
								CalendarDialogAuditSupport.moveDisplayedMonth(fixture, 1);
							}
						}));
			}
			for (int i = 0; i < 30; i++)
			{
				profile.addSample("monthBackward",
						CalendarDialogAuditSupport.measureNanos(new CalendarDialogAuditSupport.ThrowingRunnable()
						{
							public void run() throws Exception
							{
								CalendarDialogAuditSupport.moveDisplayedMonth(fixture, -1);
							}
						}));
			}

			int calendarCount = CalendarDialogAuditSupport.getCalendarCount(fixture);
			if (calendarCount > 1)
			{
				for (int i = 1; i < Math.min(calendarCount, 6); i++)
				{
					final int index = i;
					profile.addSample("calendarSwitch",
							CalendarDialogAuditSupport.measureNanos(new CalendarDialogAuditSupport.ThrowingRunnable()
							{
								public void run() throws Exception
								{
									CalendarDialogAuditSupport.selectCalendar(fixture, index);
								}
							}));
				}
			}

			for (int i = 0; i < 10; i++)
			{
				final int index = i % 7;
				profile.addSample("weekdaySelection",
						CalendarDialogAuditSupport.measureNanos(new CalendarDialogAuditSupport.ThrowingRunnable()
						{
							public void run() throws Exception
							{
								CalendarDialogAuditSupport.selectWeekDay(fixture, index);
							}
						}));
			}

			CalendarDialogAuditSupport.createSingleDayException(fixture);
			if (CalendarDialogAuditSupport.getExceptionCount(fixture) > 0)
			{
				for (int i = 0; i < 5; i++)
				{
					profile.addSample("exceptionSelection",
							CalendarDialogAuditSupport.measureNanos(new CalendarDialogAuditSupport.ThrowingRunnable()
							{
								public void run() throws Exception
								{
									CalendarDialogAuditSupport.selectException(fixture, 0);
								}
							}));
				}
			}

			for (int i = 0; i < 5; i++)
			{
				final int source = i % 7;
				final int target = (i + 1) % 7;
				profile.addSample("save",
						CalendarDialogAuditSupport.measureNanos(new CalendarDialogAuditSupport.ThrowingRunnable()
						{
							public void run() throws Exception
							{
								CalendarDialogAuditSupport.editWorkingHoursAndTriggerSave(fixture, source, target);
							}
						}));
			}

			profile.addSample("close",
					CalendarDialogAuditSupport.measureNanos(new CalendarDialogAuditSupport.ThrowingRunnable()
					{
						public void run() throws Exception
						{
							CalendarDialogAuditSupport.closeViaCancel(fixture);
						}
					}));
		}
		finally
		{
			System.out.println("Calendar dialog performance profile");
			System.out.println(profile.format());
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}

		CalendarDialogAuditSupport.assertWithinBudget(this, "open", profile.summarize("open"), MAX_OPEN_MILLIS);
		CalendarDialogAuditSupport.assertWithinBudget(this, "monthForward", profile.summarize("monthForward"), MAX_INTERACTION_MILLIS);
		CalendarDialogAuditSupport.assertWithinBudget(this, "monthBackward", profile.summarize("monthBackward"), MAX_INTERACTION_MILLIS);
		CalendarDialogAuditSupport.assertWithinBudget(this, "calendarSwitch", profile.summarize("calendarSwitch"), MAX_INTERACTION_MILLIS);
		CalendarDialogAuditSupport.assertWithinBudget(this, "weekdaySelection", profile.summarize("weekdaySelection"),
				MAX_INTERACTION_MILLIS);
		CalendarDialogAuditSupport.assertWithinBudget(this, "exceptionSelection", profile.summarize("exceptionSelection"),
				MAX_INTERACTION_MILLIS);
		CalendarDialogAuditSupport.assertWithinBudget(this, "save", profile.summarize("save"), MAX_INTERACTION_MILLIS);
		CalendarDialogAuditSupport.assertWithinBudget(this, "close", profile.summarize("close"), MAX_CLOSE_MILLIS);
	}

	private void warmUpDialog() throws Exception
	{
		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			CalendarDialogAuditSupport.closeViaCancel(fixture);
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}
}
