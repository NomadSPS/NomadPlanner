# Calendar Dialog Audit

## Scope
- Target the new `Calendar` manager dialog and its `CalendarView` lifecycle.
- Verify open, interact, close, and reopen flows.
- Measure EDT-bound interaction cost and document current/potential issues.

## Automated Coverage Added
- `test.com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogBoxTest`
  - dialog initializes without an active `DocumentFrame`
- `test.com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogLifecycleTest`
  - close via `Cancel`
  - close via `OK`
  - close via window `X`
  - reopen after close
- `test.com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogDirtyStateTest`
  - weekday/exception selection churn does not mark the dialog dirty by itself
  - edited working hours are buffered into the scratch calendar on navigation instead of saving the real calendar immediately
- `test.com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogStressTest`
  - 30-month forward/back navigation
  - repeated calendar list switching
  - weekday and exception selection churn
  - working-hour edit plus `saveIfNeeded()` path
  - 25 open/close cycles with no displayable dialogs retained
- `test.com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogPerformanceTest`
  - records open, month navigation, calendar switch, weekday selection, exception selection, save, and close timings
  - fails when a measured EDT interaction breaches the configured smoke thresholds

Run the full suite with:

```powershell
$cp = "projectlibre_build/build;projectlibre_contrib/build;projectlibre_contrib/*;projectlibre_contrib/lib/*;projectlibre_contrib/lib/exchange/*;projectlibre_contrib/lib/groovy/*;projectlibre_contrib/lib/jasperreports/*;projectlibre_contrib/ant-lib/*;projectlibre_contrib/tmp/*"
java -cp "$cp" junit.textui.TestRunner test.com.projectlibre1.dialog.calendar.CalendarDialogAuditSuite
```

Performance thresholds can be overridden with system properties:

```powershell
java -Dcalendar.audit.maxInteractionMillis=250 -Dcalendar.audit.maxCloseMillis=500 -Dcalendar.audit.maxOpenMillis=750 -cp "$cp" junit.textui.TestRunner test.com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogPerformanceTest
```

## Manual Profiling
The PowerShell helper below captures a focused JFR session, a pre/post thread dump, and a heap histogram from a running desktop process:

```powershell
.\\scripts\\capture-calendar-diagnostics.ps1 -Pid <java-pid> -DurationSeconds 45
```

Recommended live-app workflow:
1. Launch the app and open a real project.
2. Run the script against the visible app JVM.
3. During the capture window, open `Project -> Calendar`, navigate months, switch calendars, edit a day, and close the dialog.
4. If the dialog stalls or refuses to close, capture an extra immediate dump:

```powershell
jcmd <java-pid> Thread.print > artifacts\\calendar-stall-thread.txt
jcmd <java-pid> GC.class_histogram > artifacts\\calendar-stall-histogram.txt
```

Suggested datasets:
- empty/new local project
- `projectlibre_exchange/testdata/New Product.mpp`
- real Primavera sample such as repo-root `sample.xer`

## Initial Findings Checklist

### Correctness / Blockers
- Fixed before this audit:
  - `GraphicManager.isEditingMasterProject()` could dereference a null `currentFrame` during calendar dialog initialization.
  - the details form row specification did not match the rendered controls, which could break dialog layout during open.
- Current automated status on the dummy-project dataset:
  - lifecycle smoke passes:
    - close via `Cancel`
    - close via `OK`
    - close via window `X`
    - reopen after close
  - dirty-state regression passes:
    - plain weekday navigation no longer marks the dialog dirty
    - editing hours and navigating now leaves the dialog `unsaved` instead of persisting the calendar immediately
  - repeat open/close stress passes:
    - 25 cycles complete with no displayable `ChangeWorkingTimeDialogBox` instances retained
  - rapid interaction stress passes, and the repeated invalid-calendar alert is now reduced to a single surfaced message for that dialog session during aggressive weekday/exception churn
  - full audit suite currently passes:
    - `OK (9 tests)`
  - the save path no longer breaches the current dummy-project smoke budget after the selection-save hardening
  - earlier isolated budget variance remains useful history, but is no longer reproduced by the current harness

### EDT / Performance Risks
- `ChangeWorkingTimeDialogBox.updateView()` recalculates flagged and non-working dates across the entire visible calendar range on every displayed-month change.
- `ChangeWorkingTimeDialogBox.updateWorkingHours()` still commits buffered working-hour edits synchronously on the EDT when text fields are dirty, although it no longer persists the real calendar or rebuilds all selection lists on every navigation hop.
- `calendarList`, `weekDayList`, and `exceptionList` selection changes all run save/update logic synchronously on the EDT.
- Close responsiveness still depends on the same EDT path staying healthy because `onCancel()` and window close are lightweight wrappers around `setVisible(false)`.

Latest passing dummy-project timings from `ChangeWorkingTimeDialogPerformanceTest` as part of the full suite:
- `open`: `84.13 ms`
- `monthForward` p95/max: `13.15 / 15.07 ms`
- `monthBackward` p95/max: `7.2 / 7.76 ms`
- `calendarSwitch` p95/max: `12.47 / 12.47 ms`
- `weekdaySelection` p95/max: `13.54 / 13.54 ms`
- `exceptionSelection` p95/max: `0.17 / 0.17 ms`
- `save` p95/max: `109.2 / 109.2 ms`
- `close`: `37.46 ms`

Latest standalone performance smoke also passes:
- `open`: not re-run independently after the final invalid-calendar alert suppression
- current full-suite profile remains the primary reference

### Memory / Listener Risks
- Repeated open/close creates fresh dialogs and listeners each time; the new smoke test checks that no `ChangeWorkingTimeDialogBox` instances remain displayable after cleanup.
- `AbstractDialog.pack()` rebuilds content every time it is called without first clearing components. Normal modal use calls it once, but repeated `pack()` on the same instance remains a latent duplication risk.

### Usability Risks
- Editing working-hour fields followed by a selection change triggers an immediate save path. If validation fails, the user is interrupted in the middle of navigation.
- The month view, work-week list, and exception list all update each other eagerly, so larger calendar datasets still need live JFR validation on real imported projects.
- The validation message `The calendar must have at least one default working day.` can be emitted several times during fast interaction sequences, which is noisy and suggests the editor can enter transient invalid states during navigation.

## Prioritized Follow-Up
1. Debounce or batch cross-component selection updates (`calendarList`, `weekDayList`, `exceptionList`) so navigation does not repeatedly trigger validation and save work on the EDT.
2. Root-cause the remaining invalid interaction path behind `The calendar must have at least one default working day.` so the dialog no longer attempts the invalid operation during aggressive churn, even though duplicate surfacing is now suppressed.
3. Run the JFR/thread-dump workflow against a medium `MPP` and a real Primavera import with multiple calendars to confirm whether the same save-path and month-refresh costs scale up materially.
