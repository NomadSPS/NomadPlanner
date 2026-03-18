# Primavera P6 Import Implementation Checklist

Goal: add ProjectLibre import support for Primavera P6 `XER` and `PMXML` exports by reusing the vendored MPXJ readers where possible.

## Phase 1: Read-path spike

- [x] Confirm the repo already vendors MPXJ Primavera readers.
- [x] Add a repo-local inspection utility to summarize real Primavera files before changing the importer.
- [x] Add fixture guidance for local `XER` / `PMXML` samples.
- [ ] Validate at least one real single-project `XER` export.
- [ ] Validate at least one real single-project `PMXML` export.
- [ ] Validate at least one multi-project `XER` export.
- [ ] Record concrete gaps from the sample files:
  - reader failures
  - missing calendars
  - missing baselines
  - WBS / task hierarchy issues
  - dependency / lag issues
  - assignment / contour / timephased issues

## Phase 2: Reader selection in the importer

- [ ] Refactor [MspImporter.java](C:/Users/osama/source/pm-opensource/projectlibre-code/projectlibre-code/projectlibre_exchange/src/com/projectlibre/core/pm/exchange/MspImporter.java) to choose the reader by file content and extension.
- [ ] Keep `ImprovedMSPDIReader` for MSPDI so existing large-XML and timephased behavior is preserved.
- [ ] Add explicit support for:
  - `.xer`
  - `.pmxml`
  - `.xml` content-sniffing between MSPDI and PMXML
- [ ] Fail with a format-specific error message instead of a generic import error where possible.

## Phase 3: Multi-project handling

- [ ] Decide the MVP behavior for multi-project Primavera files.
- [ ] Prefer an explicit project-selection dialog for `XER` files with multiple projects.
- [ ] Confirm how `PMXML` multi-project exports should be handled because the vendored PMXML reader currently selects one non-external project.

## Phase 4: Conversion audit

- [ ] Verify the existing MPXJ-to-ProjectLibre converters work with Primavera `ProjectFile` data:
  - tasks
  - resources
  - calendars
  - dependencies
  - project header / options
- [ ] Audit baseline import behavior for Primavera data.
- [ ] Decide MVP treatment for assignment timephased values.
- [ ] Decide MVP treatment for cross-project predecessor links.

## Phase 5: UI and file chooser

- [ ] Expand [FileHelper.java](C:/Users/osama/source/pm-opensource/projectlibre-code/projectlibre-code/projectlibre_core/src/com/projectlibre1/session/FileHelper.java) to admit Primavera extensions in open flows.
- [ ] Update user-visible filter labels so `XER` and `PMXML` are discoverable.
- [ ] Keep save/export behavior unchanged for now.

## Phase 6: Regression coverage

- [ ] Add fixture-backed importer coverage for:
  - single-project `XER`
  - multi-project `XER`
  - single-project `PMXML`
  - edge-case calendars / baselines
- [ ] Assert at minimum:
  - task count
  - resource count
  - assignment count
  - dependency count
  - calendar count
  - representative baseline fields

## Phase 7: Release hardening

- [ ] Re-test `MPP`, `MPX`, `MSPDI`, and `planner` imports for regressions.
- [ ] Review whether the vendored MPXJ snapshot is sufficient or whether an MPXJ refresh is justified.
- [ ] Update any import help text and release notes.

## Notes

- Phase 1 has started in-repo with:
  - [PrimaveraProjectLibreProbe.java](C:/Users/osama/source/pm-opensource/projectlibre-code/projectlibre-code/projectlibre_exchange/src/net/sf/mpxj/sample/PrimaveraProjectLibreProbe.java)
  - [README.md](C:/Users/osama/source/pm-opensource/projectlibre-code/projectlibre-code/projectlibre_exchange/testdata/primavera/README.md)
- The current highest-risk areas are:
  - the `.xml` split between MSPDI and PMXML
  - multi-project selection
  - Primavera-specific baseline and assignment behavior
  - the age of the vendored MPXJ code
