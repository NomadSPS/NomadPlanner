# Primavera Spike Fixtures

This directory is for local Primavera P6 sample files used during phase 1 of the import spike.

Do not commit licensed customer exports unless they are cleared for inclusion.

The current regression sample is an optional local file at the repo root named `sample.xer`.
The regression test skips cleanly when that file is absent.

Recommended local fixture set:

- `single-project.xer`
- `multi-project.xer`
- `single-project.pmxml`
- `single-project.xml`

Suggested checks for each file:

- Can MPXJ parse it?
- How many projects are present?
- Are task, resource, assignment, calendar, and dependency counts reasonable?
- Are baseline start/finish values present where expected?
- Does WBS / hierarchy look correct?

Inspection utility added for this spike:

- [PrimaveraProjectLibreProbe.java](C:/Users/osama/source/pm-opensource/projectlibre-code/projectlibre-code/projectlibre_exchange/src/net/sf/mpxj/sample/PrimaveraProjectLibreProbe.java)

Regression coverage:

- [PrimaveraXerSampleTest.java](C:/Users/osama/source/pm-opensource/projectlibre-code/projectlibre-code/projectlibre_exchange/src/test/com/projectlibre1/exchange/PrimaveraXerSampleTest.java)

Typical usage after compiling the exchange module:

```powershell
cd projectlibre_exchange
ant build
```

Then run the probe from your IDE or an existing project classpath with:

```text
net.sf.mpxj.sample.PrimaveraProjectLibreProbe <path-to-file>
```

The regression test asserts the current sample parses as one Primavera XER project with:

- 538 tasks
- 6 calendars
- 613 predecessor links
- 0 resources
- 0 assignments
