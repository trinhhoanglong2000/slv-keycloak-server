@echo off
setlocal enabledelayedexpansion

:: Define your modules
set modules=slv-keycloak-core slv-keycloak-rest slv-keycloak-ui

:: Destination folder
set dest=keycloak-providers

:: Create destination folder if it doesn't exist
if not exist "%dest%" (
    mkdir "%dest%"
)

:: Loop through each module
for %%m in (%modules%) do (
    echo Processing module: %%m

    set "source=%%m\target"
    set "pattern=com.soloval.tech.%%m-*.jar"

    :: Check if source folder exists
    if exist "!source!" (
        echo Looking for !pattern! in !source!
        for %%f in (!source!\!pattern!) do (
            echo Copying %%f to %dest%
            copy /Y "%%f" "%dest%"
        )
    ) else (
        echo Folder !source! does not exist. Skipping.
    )
)

echo Done.
pause