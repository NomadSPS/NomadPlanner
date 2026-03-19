package test.com.projectlibre1.dialog.calendar;

import junit.framework.TestCase;

public class ChangeWorkingTimeDialogStressTest extends TestCase
{
	public void testRapidInteractionFlowDoesNotThrowOrFreezeClosePath() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			for (int i = 0; i < 30; i++)
			{
				CalendarDialogAuditSupport.moveDisplayedMonth(fixture, 1);
			}
			for (int i = 0; i < 30; i++)
			{
				CalendarDialogAuditSupport.moveDisplayedMonth(fixture, -1);
			}

			int calendarCount = CalendarDialogAuditSupport.getCalendarCount(fixture);
			if (calendarCount > 1)
			{
				for (int i = 0; i < 12; i++)
				{
					CalendarDialogAuditSupport.selectCalendar(fixture, i % calendarCount);
				}
			}

			for (int i = 0; i < 14; i++)
			{
				CalendarDialogAuditSupport.selectWeekDay(fixture, i % 7);
			}

			CalendarDialogAuditSupport.createSingleDayException(fixture);
			assertTrue(CalendarDialogAuditSupport.getExceptionCount(fixture) > 0);
			for (int i = 0; i < 6; i++)
			{
				CalendarDialogAuditSupport.selectException(fixture, 0);
				CalendarDialogAuditSupport.selectWeekDay(fixture, i % 7);
			}

			for (int i = 0; i < 5; i++)
			{
				CalendarDialogAuditSupport.editWorkingHoursAndTriggerSave(fixture, i % 7, (i + 1) % 7);
			}

			assertTrue(CalendarDialogAuditSupport.isShowing(fixture));
			CalendarDialogAuditSupport.closeViaCancel(fixture);
			assertFalse(CalendarDialogAuditSupport.isShowing(fixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}

	public void testRepeatedOpenCloseCyclesDoNotLeaveDisplayableDialogsBehind() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		for (int i = 0; i < 25; i++)
		{
			CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
			try
			{
				if ((i % 3) == 0)
				{
					CalendarDialogAuditSupport.closeViaCancel(fixture);
				}
				else if ((i % 3) == 1)
				{
					CalendarDialogAuditSupport.closeViaOk(fixture);
				}
				else
				{
					CalendarDialogAuditSupport.closeViaWindow(fixture);
				}
			}
			finally
			{
				CalendarDialogAuditSupport.disposeFixture(fixture);
			}
			assertEquals("Dialog instances should not stay displayable after cycle " + i, 0,
					CalendarDialogAuditSupport.countDisplayableCalendarDialogs());
		}
	}
}
