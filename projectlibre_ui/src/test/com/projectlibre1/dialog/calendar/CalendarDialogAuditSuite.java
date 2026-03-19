package test.com.projectlibre1.dialog.calendar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public final class CalendarDialogAuditSuite extends TestCase
{
	public CalendarDialogAuditSuite()
	{
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite("CalendarDialogAuditSuite");
		suite.addTestSuite(ChangeWorkingTimeDialogBoxTest.class);
		suite.addTestSuite(ChangeWorkingTimeDialogDirtyStateTest.class);
		suite.addTestSuite(ChangeWorkingTimeDialogSelectionModelTest.class);
		suite.addTestSuite(ChangeWorkingTimeDialogLifecycleTest.class);
		suite.addTestSuite(ChangeWorkingTimeDialogStressTest.class);
		suite.addTestSuite(ChangeWorkingTimeDialogPerformanceTest.class);
		return suite;
	}
}
