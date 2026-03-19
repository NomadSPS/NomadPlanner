package test.com.projectlibre1.dialog.calendar;

import junit.framework.TestCase;

public class ChangeWorkingTimeDialogDirtyStateTest extends TestCase
{
	public void testSelectionChangesDoNotMarkWorkingHoursDirty() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			CalendarDialogAuditSupport.selectWeekDay(fixture, 0);
			assertFalse(CalendarDialogAuditSupport.isDirtyWorkingHours(fixture));
			assertFalse(CalendarDialogAuditSupport.isUnsaved(fixture));

			CalendarDialogAuditSupport.selectWeekDay(fixture, 1);
			assertFalse(CalendarDialogAuditSupport.isDirtyWorkingHours(fixture));
			assertFalse(CalendarDialogAuditSupport.isUnsaved(fixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}

	public void testNavigationBuffersEditedHoursWithoutSavingCalendarImmediately() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			CalendarDialogAuditSupport.editWorkingHoursAndTriggerSave(fixture, 0, 1);
			assertFalse(CalendarDialogAuditSupport.isDirtyWorkingHours(fixture));
			assertTrue(CalendarDialogAuditSupport.isUnsaved(fixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}
}
