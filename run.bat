@echo off
setlocal EnableDelayedExpansion

rem === App locations ===
set "APP_DIR=%~dp0"
pushd "%APP_DIR%"

set "DIST=%APP_DIR%dist"
set "APP_NAME=BW2-TCME"
set "JAR=%DIST%\%APP_NAME%.jar"

rem === Build if needed ===
if not exist "%JAR%" (
  echo [run] JAR not found. Building...
  call "%APP_DIR%build.bat" || (popd & exit /b 1)
)
if not exist "%JAR%" (
  echo [run] ERROR: Still no JAR at "%JAR%". Build must have failed.
  popd & exit /b 1
)

rem === Put external tools on PATH (prefer /tools, else /resources/tools) ===
set "TOOLS_DIR=%APP_DIR%tools"
if not exist "%TOOLS_DIR%" set "TOOLS_DIR=%APP_DIR%resources\tools"
if exist "%TOOLS_DIR%" set "PATH=%TOOLS_DIR%;%PATH%"

rem === Choose a Java runtime ===
set "JAVA_EXE="
if exist "%APP_DIR%jre\bin\java.exe" set "JAVA_EXE=%APP_DIR%jre\bin\java.exe"
if not defined JAVA_EXE if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
if not defined JAVA_EXE set "JAVA_EXE=java"

echo [run] Using Java: %JAVA_EXE%
echo [run] Tools dir: %TOOLS_DIR%
echo [run] Launching %JAR% ...

"%JAVA_EXE%" -jar "%JAR%"
set "ERR=%ERRORLEVEL%"

popd
exit /b %ERR%
