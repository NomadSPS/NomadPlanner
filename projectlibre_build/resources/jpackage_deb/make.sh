#!/bin/bash

APP_VERSION="@version@"

rm -rf app
mkdir -p app
rm -f NomadPlan-${APP_VERSION}.rpm
jpackage --type deb --input source --dest app --name NomadPlan --main-jar projectlibre-${APP_VERSION}.jar --icon source/projectlibre.png --app-version ${APP_VERSION} --license-file source/license/license.txt --vendor "NomadPlan" --linux-package-name projectlibre --linux-shortcut --linux-menu-group "Utility"
