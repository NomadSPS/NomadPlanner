package test.com.projectlibre1.dialog.calendar;

import junit.framework.TestCase;

public class ChangeWorkingTimeDialogSelectionModelTest extends TestCase
{
	public void testSelectingWeekdayThenExceptionKeepsOnlyExceptionTargetActive() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			CalendarDialogAuditSupport.selectWeekDay(fixture, 0);
			assertEquals("WEEKDAY", CalendarDialogAuditSupport.getSelectionKind(fixture));

			CalendarDialogAuditSupport.createSingleDayException(fixture);
			assertEquals("EXCEPTION_DAY", CalendarDialogAuditSupport.getSelectionKind(fixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}

	public void testSelectingExceptionThenWeekdayKeepsOnlyWeekdayTargetActive() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			CalendarDialogAuditSupport.createSingleDayException(fixture);
			assertEquals("EXCEPTION_DAY", CalendarDialogAuditSupport.getSelectionKind(fixture));

			CalendarDialogAuditSupport.selectWeekDay(fixture, 1);
			assertEquals("WEEKDAY", CalendarDialogAuditSupport.getSelectionKind(fixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}
}
