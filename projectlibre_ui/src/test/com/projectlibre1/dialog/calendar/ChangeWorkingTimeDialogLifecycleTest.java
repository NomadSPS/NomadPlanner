package test.com.projectlibre1.dialog.calendar;

import junit.framework.TestCase;

public class ChangeWorkingTimeDialogLifecycleTest extends TestCase
{
	public void testCancelClosesVisibleDialog() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			assertTrue(CalendarDialogAuditSupport.isShowing(fixture));
			CalendarDialogAuditSupport.closeViaCancel(fixture);
			assertFalse(CalendarDialogAuditSupport.isShowing(fixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}

	public void testOkClosesVisibleDialog() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture fixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			assertTrue(CalendarDialogAuditSupport.isShowing(fixture));
			CalendarDialogAuditSupport.closeViaOk(fixture);
			assertFalse(CalendarDialogAuditSupport.isShowing(fixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(fixture);
		}
	}

	public void testWindowCloseAndReopenRemainStable() throws Exception
	{
		if (CalendarDialogAuditSupport.isUiTestSkipped())
		{
			return;
		}

		CalendarDialogAuditSupport.DialogFixture firstFixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			assertTrue(CalendarDialogAuditSupport.isShowing(firstFixture));
			CalendarDialogAuditSupport.closeViaWindow(firstFixture);
			assertFalse(CalendarDialogAuditSupport.isShowing(firstFixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(firstFixture);
		}

		CalendarDialogAuditSupport.DialogFixture secondFixture = CalendarDialogAuditSupport.openDialogFixture();
		try
		{
			assertTrue(CalendarDialogAuditSupport.isShowing(secondFixture));
			CalendarDialogAuditSupport.closeViaCancel(secondFixture);
			assertFalse(CalendarDialogAuditSupport.isShowing(secondFixture));
		}
		finally
		{
			CalendarDialogAuditSupport.disposeFixture(secondFixture);
		}
	}
}
