/*******************************************************************************
 * The contents of this file are subject to the Common Public Attribution License
 * Version 1.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.projectlibre.com/license . The License is based on the Mozilla Public
 * License Version 1.1 but Sections 14 and 15 have been added to cover use of
 * software over a computer network and provide for limited attribution for the
 * Original Developer. In addition, Exhibit A has been modified to be consistent
 * with Exhibit B.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the
 * specific language governing rights and limitations under the License. The
 * Original Code is ProjectLibre. The Original Developer is the Initial Developer
 * and is ProjectLibre Inc. All portions of the code written by ProjectLibre are
 * Copyright (c) 2012-2019. All Rights Reserved. All portions of the code written by
 * ProjectLibre are Copyright (c) 2012-2019. All Rights Reserved. Contributor
 * ProjectLibre, Inc.
 *******************************************************************************/
package test.com.projectlibre1.dialog.calendar;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogBox;
import com.projectlibre1.pm.calendar.WorkingCalendar;
import com.projectlibre1.pm.graphic.frames.GraphicManager;
import com.projectlibre1.pm.task.Project;
import junit.framework.TestCase;

public class ChangeWorkingTimeDialogBoxTest extends TestCase
{
	public void testCreateContentPanelWithoutCurrentDocumentFrame() throws Exception
	{
		if (GraphicsEnvironment.isHeadless())
		{
			return;
		}

		final Throwable[] failure = new Throwable[1];
		final JComponent[] content = new JComponent[1];

		SwingUtilities.invokeAndWait(new Runnable()
		{
			public void run()
			{
				JFrame frame = null;
				ChangeWorkingTimeDialogBox dialog = null;
				try
				{
					frame = new JFrame();
					new GraphicManager(frame);
					Project project = CalendarDialogAuditSupport.createFreshProject();
					WorkingCalendar calendar = (WorkingCalendar) project.getWorkCalendar();
					dialog = ChangeWorkingTimeDialogBox.getInstance(frame, project, calendar, null, false, project.getUndoController());
					content[0] = dialog.createContentPanel();
				}
				catch (Throwable t)
				{
					failure[0] = t;
				}
				finally
				{
					if (dialog != null)
					{
						dialog.dispose();
					}
					if (frame != null)
					{
						frame.dispose();
					}
				}
			}
		});

		if (failure[0] != null)
		{
			failure[0].printStackTrace();
			fail("Calendar dialog should initialize without an active document frame: " + failure[0]);
		}

		assertNotNull(content[0]);
	}
}
