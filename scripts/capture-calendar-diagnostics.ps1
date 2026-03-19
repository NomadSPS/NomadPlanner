param(
    [Parameter(Mandatory = $true)]
    [int]$Pid,

    [int]$DurationSeconds = 30,

    [string]$OutputDir = ".\\artifacts\\calendar-diagnostics"
)

$ErrorActionPreference = "Stop"

$jcmd = (Get-Command jcmd -ErrorAction SilentlyContinue).Source
if (-not $jcmd) {
    throw "jcmd was not found on PATH. Run this from a JDK shell or add the JDK bin directory to PATH."
}

$timestamp = Get-Date -Format "yyyyMMdd-HHmmss"
$sessionDir = Join-Path $OutputDir "calendar-audit-$timestamp"
New-Item -ItemType Directory -Force -Path $sessionDir | Out-Null

$jfrFile = Join-Path $sessionDir "calendar-session.jfr"
$threadBefore = Join-Path $sessionDir "thread-before.txt"
$threadAfter = Join-Path $sessionDir "thread-after.txt"
$histogram = Join-Path $sessionDir "class-histogram.txt"
$meta = Join-Path $sessionDir "session.txt"

"PID=$Pid" | Set-Content $meta
"Started=$(Get-Date -Format o)" | Add-Content $meta
"DurationSeconds=$DurationSeconds" | Add-Content $meta

& $jcmd $Pid Thread.print | Set-Content $threadBefore
& $jcmd $Pid JFR.start name=CalendarAudit settings=profile duration="${DurationSeconds}s" filename="$jfrFile" | Add-Content $meta

Start-Sleep -Seconds ($DurationSeconds + 2)

& $jcmd $Pid Thread.print | Set-Content $threadAfter
& $jcmd $Pid GC.class_histogram | Set-Content $histogram

"Finished=$(Get-Date -Format o)" | Add-Content $meta
Write-Host "Calendar diagnostics captured in $sessionDir"
