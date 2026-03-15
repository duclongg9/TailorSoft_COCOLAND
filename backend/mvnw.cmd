@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup script for Windows, version 3.2.0
@REM
@REM Optional ENV vars
@REM   JAVA_HOME - Java installation directory
@REM   MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@echo off
@setlocal

set ERROR_CODE=0

@REM To isolate internal variables from possible wrapper v1 variables
@set "_MVNW_GOALS=%*"
@set "_MVNW_REPO_PATH="
@set "_MVNW_WRAPPER_VERSION=3.2.0"

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

for %%i in (java.exe) do set "JAVACMD=%%~$PATH:i"
if not "%JAVACMD%" == "" goto OkJHome

echo.
echo Error: JAVA_HOME is not defined correctly.
echo   We cannot execute %JAVACMD%
echo.
set ERROR_CODE=1
goto end

:OkJHome
if "%JAVACMD%" == "" set "JAVACMD=%JAVA_HOME%\bin\java.exe"

if exist "%JAVACMD%" goto checkWrapper

echo.
echo Error: JAVA_HOME is set to an invalid directory.
echo   JAVA_HOME = "%JAVA_HOME%"
echo   Please set the JAVA_HOME variable in your environment to match the
echo   location of your Java installation.
echo.
set ERROR_CODE=1
goto end
@REM ==== END VALIDATION ====

:checkWrapper
set "APP_BASE_NAME=%~n0"
set "APP_HOME=%~dp0"

@REM Resolve how to target the agent jar
set "WRAPPER_JAR=%APP_HOME%.mvn\wrapper\maven-wrapper.jar"
set "WRAPPER_PROPERTIES=%APP_HOME%.mvn\wrapper\maven-wrapper.properties"

if exist "%WRAPPER_JAR%" goto run

@REM Download wrapper jar if it doesn't exist
set "DOWNLOAD_URL="
if exist "%WRAPPER_PROPERTIES%" (
    for /f "tokens=1,2 delims==" %%A in (%WRAPPER_PROPERTIES%) do (
        if "%%A"=="wrapperUrl" set "DOWNLOAD_URL=%%B"
    )
)

if "%DOWNLOAD_URL%" == "" (
    set "DOWNLOAD_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/%_MVNW_WRAPPER_VERSION%/maven-wrapper-%_MVNW_WRAPPER_VERSION%.jar"
)

echo Downloading Maven Wrapper from %DOWNLOAD_URL%
powershell -Command "&{"^
    "$webclient = New-Object System.Net.WebClient;"^
    "if (-not $webclient.Proxy.IsBypassed('https://repo.maven.apache.org')) {"^
    "  $webclient.Proxy = [System.Net.WebRequest]::GetSystemWebProxy();"^
    "  $webclient.Proxy.Credentials = [System.Net.CredentialCache]::DefaultNetworkCredentials;"^
    "}"^
    "$webclient.DownloadFile('%DOWNLOAD_URL%', '%WRAPPER_JAR%');"^
    "}"

if not exist "%WRAPPER_JAR%" (
    echo.
    echo Error: Failed to download Maven Wrapper jar.
    echo.
    set ERROR_CODE=1
    goto end
)

:run
set "MAVEN_PROJECT_DIR=%APP_HOME%"
if "%MAVEN_PROJECT_DIR:~-1%"=="\" set "MAVEN_PROJECT_DIR=%MAVEN_PROJECT_DIR:~0,-1%"

"%JAVACMD%" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECT_DIR%" org.apache.maven.wrapper.MavenWrapperMain %*

:end
@setlocal disableDelayedExpansion
@exit /b %ERROR_CODE%
