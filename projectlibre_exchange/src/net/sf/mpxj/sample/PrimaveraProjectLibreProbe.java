/*
 * file:       PrimaveraProjectLibreProbe.java
 * author:     OpenAI Codex
 * date:       2026-03-18
 */

/*
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */

package net.sf.mpxj.sample;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectProperties;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.primavera.PrimaveraXERFileReader;
import net.sf.mpxj.reader.UniversalProjectReader;

/**
 * Phase-1 Primavera inspection utility for ProjectLibre import work.
 * It is intentionally read-only and prints a compact summary of the
 * project data MPXJ can see in P6 files.
 */
public final class PrimaveraProjectLibreProbe
{
   /**
    * Command line entry point.
    *
    * @param args command line arguments
    */
   public static void main(String[] args)
   {
      try
      {
         if (args.length != 1)
         {
            System.out.println("Usage: PrimaveraProjectLibreProbe <input file>");
            System.exit(1);
         }

         PrimaveraProjectLibreProbe probe = new PrimaveraProjectLibreProbe();
         probe.process(args[0]);
         System.exit(0);
      }

      catch (Exception ex)
      {
         System.out.println();
         System.out.print("Probe Error: ");
         ex.printStackTrace(System.out);
         System.out.println();
         System.exit(1);
      }
   }

   /**
    * Inspect a Primavera file and print summary data.
    *
    * @param fileName file to inspect
    * @throws Exception on read errors
    */
   public void process(String fileName) throws Exception
   {
      File file = new File(fileName);
      if (!file.isFile())
      {
         throw new IllegalArgumentException("File not found: " + file.getAbsolutePath());
      }

      String lowerName = file.getName().toLowerCase(Locale.ROOT);
      if (lowerName.endsWith(".xer"))
      {
         inspectXER(file);
      }
      else
      {
         inspectSingleProjectFile(file);
      }
   }

   /**
    * Inspect an XER file using the Primavera reader directly so we can
    * see whether multiple projects are present.
    *
    * @param file XER file
    * @throws Exception on read errors
    */
   private void inspectXER(File file) throws Exception
   {
      try (InputStream stream = new BufferedInputStream(new FileInputStream(file)))
      {
         PrimaveraXERFileReader reader = new PrimaveraXERFileReader();
         List<ProjectFile> projects = reader.readAll(stream, false);

         System.out.println("File: " + file.getAbsolutePath());
         System.out.println("Detected format: XER");
         System.out.println("Projects parsed: " + projects.size());

         int index = 1;
         for (ProjectFile project : projects)
         {
            printProjectSummary(index++, project);
         }
      }
   }

   /**
    * Inspect a PMXML file or any file auto-detected by UniversalProjectReader.
    *
    * @param file input file
    * @throws Exception on read errors
    */
   private void inspectSingleProjectFile(File file) throws Exception
   {
      ProjectFile project = new UniversalProjectReader().read(file);
      if (project == null)
      {
         throw new IllegalArgumentException("Unsupported or unrecognized file type");
      }

      System.out.println("File: " + file.getAbsolutePath());
      printProjectSummary(1, project);

      ProjectProperties properties = project.getProjectProperties();
      if (!"Primavera".equalsIgnoreCase(safe(properties.getFileApplication()))
         && !"PMXML".equalsIgnoreCase(safe(properties.getFileType()))
         && !"XER".equalsIgnoreCase(safe(properties.getFileType())))
      {
         System.out.println("Warning: this file was not detected as a Primavera file.");
      }
   }

   /**
    * Print a compact summary for one parsed project.
    *
    * @param index display index
    * @param project parsed project
    */
   private void printProjectSummary(int index, ProjectFile project)
   {
      ProjectProperties properties = project.getProjectProperties();
      Stats stats = new Stats();

      for (Task task : project.getTasks())
      {
         if (task == null)
         {
            continue;
         }

         stats.tasks++;
         if (task.getSummary())
         {
            stats.summaryTasks++;
         }
         else
         {
            stats.leafTasks++;
         }

         if (task.getMilestone())
         {
            stats.milestones++;
         }

         if (task.getBaselineStart() != null || task.getBaselineFinish() != null)
         {
            stats.tasksWithBaseline++;
         }

         stats.dependencies += task.getPredecessors().size();
      }

      for (Resource resource : project.getResources())
      {
         if (resource != null)
         {
            stats.resources++;
         }
      }

      for (ResourceAssignment assignment : project.getResourceAssignments())
      {
         if (assignment == null)
         {
            continue;
         }

         stats.assignments++;
         if (assignment.getBaselineStart() != null || assignment.getBaselineFinish() != null)
         {
            stats.assignmentsWithBaseline++;
         }
      }

      stats.calendars = project.getCalendars().size();

      System.out.println();
      System.out.println("Project " + index);
      System.out.println("  application: " + safe(properties.getFileApplication()));
      System.out.println("  file type: " + safe(properties.getFileType()));
      System.out.println("  title/id: " + safe(properties.getProjectTitle()));
      System.out.println("  name: " + safe(properties.getName()));
      System.out.println("  start: " + safe(properties.getStartDate()));
      System.out.println("  finish: " + safe(properties.getFinishDate()));
      System.out.println("  status date: " + safe(properties.getStatusDate()));
      System.out.println("  calendars: " + stats.calendars);
      System.out.println("  tasks: " + stats.tasks);
      System.out.println("  leaf tasks: " + stats.leafTasks);
      System.out.println("  summary tasks: " + stats.summaryTasks);
      System.out.println("  milestones: " + stats.milestones);
      System.out.println("  tasks with baseline: " + stats.tasksWithBaseline);
      System.out.println("  resources: " + stats.resources);
      System.out.println("  assignments: " + stats.assignments);
      System.out.println("  assignments with baseline: " + stats.assignmentsWithBaseline);
      System.out.println("  predecessor links: " + stats.dependencies);
   }

   /**
    * Render nullable values consistently.
    *
    * @param value raw value
    * @return formatted value
    */
   private String safe(Object value)
   {
      return value == null ? "<none>" : value.toString();
   }

   /**
    * Simple mutable counters for summary output.
    */
   private static final class Stats
   {
      private int assignments;
      private int assignmentsWithBaseline;
      private int calendars;
      private int dependencies;
      private int leafTasks;
      private int milestones;
      private int resources;
      private int summaryTasks;
      private int tasks;
      private int tasksWithBaseline;
   }
}
