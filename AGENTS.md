# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Project Overview

ProjectLibre is an open-source desktop project management application (Microsoft Project alternative) built with Java Swing. Version 1.9.8, targeting Java 21. Licensed under CPAL / ProjectLibre End-User License.

## Build System

**Apache Ant** with build files in `projectlibre_build/`.

```bash
# All commands run from projectlibre_build/
cd projectlibre_build

ant compile    # Compile all modules
ant dist       # Compile + create JARs (default target)
ant fatjar     # Create single executable JAR with all dependencies
ant clean      # Remove build artifacts

# Platform packaging
ant zip        # Windows ZIP distribution
ant tar        # Linux tar.gz distribution
ant rpm        # RPM package (Linux)
ant deb        # DEB package (Debian/Ubuntu)
ant jpackage-msi  # Windows MSI installer via jpackage
ant jpackage-dmg  # macOS DMG via jpackage
ant jpackage-deb  # Linux DEB via jpackage
```

Build properties are in `projectlibre_build/build.properties`. Java source/target level is 21.

## Running the Application

Main entry point: `com.projectlibre1.main.Main` (in projectlibre_ui module).

```bash
# After building
java -jar projectlibre_build/packages/projectlibre-1.9.8.jar
```

## Module Architecture

```
projectlibre_core/     - Core PM engine: scheduling, critical path, calendars, algorithms
projectlibre_ui/       - Swing GUI: Gantt charts, dialogs, ribbon toolbar, printing
projectlibre_exchange/ - File format I/O (MPP, MPX, XML, MSPDI via MPXJ library)
projectlibre_reports/  - JasperReports integration for report generation
projectlibre_contrib/  - Third-party JARs and contributed source code
projectlibre_build/    - Ant build scripts, resources, platform packaging configs
```

All Java source lives under `com.projectlibre1` package namespace.

## Key Architecture Patterns

**Startup flow**: `Main.main()` → `GanttMain.main()` → `ApplicationStartupFactory` → `MainFrameFactory.createMainFrame()`

**Gantt chart MVC**:
- `GanttModel` (data) / `GanttRenderer` (rendering) / `GanttInteractor` (input) / `Gantt` (view)
- Located in `projectlibre_ui/src/com/projectlibre1/pm/graphic/gantt/`

**Core PM model** (`projectlibre_core/src/com/projectlibre1/`):
- `pm/task/` - Task, NormalTask, Project
- `pm/resource/` - Resource, EnterpriseResource, ResourcePool
- `pm/assignment/` - Resource assignments to tasks
- `pm/dependency/` - Task dependencies and link types
- `pm/calendar/` - Working calendars
- `pm/criticalpath/` - Critical path algorithm
- `pm/scheduling/` - Scheduling engine
- `algorithm/` - Core scheduling algorithms
- `command/` - Command pattern (undo/redo support)

**Configuration system**: XML-driven field dictionary (`configuration.xml`, `view.xml`) with dynamic property binding via reflection. Supports 30+ languages via resource bundles in `strings/`.

**UI framework** (`projectlibre_ui/src/com/projectlibre1/`):
- `pm/graphic/frames/` - MainFrame, DocumentFrame, GraphicManager (large file, ~116KB)
- `dialog/` - Modal dialogs
- `toolbar/` - Ribbon-style toolbar
- `menu/` - Menu system

**Factory pattern** used extensively: `MainFrameFactory`, `ApplicationStartupFactory`, `ProjectFactory`, `ResourcePoolFactory`.

**Event-driven**: `ProjectListener`/`ProjectEvent` for change notifications, `DocumentSelectedEvent` for frame selection.

## Dependencies

All third-party JARs are in `projectlibre_contrib/lib/` (not fetched from a repository):
- **MPXJ** - Microsoft Project format reading/writing
- **Apache POI/Jakarta** - Excel format support
- **JFreeChart** - Charting
- **JasperReports** - Report generation
- **Apache Commons** (beanutils, collections, digester, logging, pool, lang)
- **Flamingo Ribbon** - Modern ribbon UI components
- **JAXB** - XML binding

## Testing

Tests are excluded from the Ant build (`<exclude name="test/**" />`). Test sources exist in `projectlibre_exchange/src/test/`. There is no automated test runner configured — tests are run via IDE.

## Important Notes

- The `build_contrib` property in `build.properties` controls whether contrib JARs are pre-built or compiled from source
- Version is set in `build.properties` and injected into `version.properties` at build time
- The CPAL license requires the ProjectLibre logo (min 144x31px) to be visible on every UI screen, linked back to projectlibre.com
