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
  - invalid `OK` keeps the dialog open
  - `Cancel` still closes cleanly after an invalid `OK`
  - reopen after close
- `test.com.projectlibre1.dialog.calendar.ChangeWorkingTimeDialogSelectionModelTest`
  - weekday selection replaces exception/date selection
  - exception selection replaces weekday selection
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
- Current automated status on the dummy-project dataset after the structural-hardening pass:
  - lifecycle smoke passes:
    - close via `Cancel`
    - close via `OK`
    - close via window `X`
    - invalid `OK` leaves the dialog open
    - `Cancel` still closes and restores the original calendar state after an invalid `OK`
    - reopen after close
  - normalized selection-model regression passes:
    - weekday selection becomes the only active target
    - exception selection becomes the only active target
  - dirty-state regression passes:
    - plain weekday navigation no longer marks the dialog dirty
    - editing hours and navigating now leaves the dialog `unsaved` instead of persisting the calendar immediately
  - repeat open/close stress passes:
    - 25 cycles complete with no displayable `ChangeWorkingTimeDialogBox` instances retained
  - rapid interaction stress passes with no surfaced invalid-calendar message under aggressive weekday/exception churn
  - full audit suite currently passes:
    - `OK (15 tests)`
  - the dialog now keeps a normalized internal selection target and routes close/cancel through scratch-state discard instead of the commit path
  - isolated test projects now use their own working-calendar copies, which removed same-JVM cross-test contamination from the audit harness

### EDT / Performance Risks
- `ChangeWorkingTimeDialogBox.updateView()` recalculates flagged and non-working dates across the entire visible calendar range on every displayed-month change.
- `ChangeWorkingTimeDialogBox.updateWorkingHours()` still resolves day descriptors synchronously on the EDT, but navigation no longer persists the real calendar or rebuilds unrelated list state on every hop.
- `calendarList`, `weekDayList`, and `exceptionList` selection changes now go through the normalized selection target and scratch-only buffering, but they still execute synchronously on the EDT.
- `Cancel` and window close now discard scratch state directly and do not run validation/persist logic, which materially reduces close-path risk.

Latest passing dummy-project timings from `ChangeWorkingTimeDialogPerformanceTest` as part of the full suite:
- `open`: `94.71 ms`
- `monthForward` p95/max: `12.22 / 16.17 ms`
- `monthBackward` p95/max: `6.63 / 8.1 ms`
- `calendarSwitch` p95/max: `9.66 / 9.66 ms`
- `weekdaySelection` p95/max: `51.9 / 51.9 ms`
- `exceptionSelection` p95/max: `0.1 / 0.1 ms`
- `save` p95/max: `97.29 / 97.29 ms`
- `close`: `21.63 ms`

Latest standalone performance smoke also passes:
- `open`: `94.71 ms`
- `save`: `97.29 ms`
- `close`: `21.63 ms`
- current full-suite profile remains the primary reference

### Memory / Listener Risks
- Repeated open/close creates fresh dialogs and listeners each time; the new smoke test checks that no `ChangeWorkingTimeDialogBox` instances remain displayable after cleanup.
- `AbstractDialog.pack()` rebuilds content every time it is called without first clearing components. Normal modal use calls it once, but repeated `pack()` on the same instance remains a latent duplication risk.

### Usability Risks
- Editing working-hour fields followed by selection changes now stays scratch-local, but larger real-world calendars still need live JFR validation because the month view, work-week list, and exception list update each other eagerly on the EDT.
- Invalid hour ranges still surface a warning when the user confirms with `OK`, which is expected, but the dialog now remains open and `Cancel` exits cleanly afterward.
- Real project datasets may still reveal heavier `updateView()` cost than the dummy-project harness, especially with more calendars and more exceptions.

## Prioritized Follow-Up
1. Run the JFR/thread-dump workflow against a medium `MPP` and a real Primavera import with multiple calendars to confirm whether `updateView()` and selection churn stay within acceptable EDT budgets on real data.
2. Consider batching or debouncing month-view and list synchronization if real-project JFR traces show meaningful EDT paint/layout spikes.
3. If user feedback still reports sluggishness, profile `refreshSelectionLists()` and flagged-date recomputation for caching opportunities before attempting broader asynchronous changes.
