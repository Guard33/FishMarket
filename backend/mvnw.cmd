@echo off
setlocal
set "BASEDIR=%~dp0"
set "WRAPPER_JAR=%BASEDIR%.mvn\wrapper\maven-wrapper.jar"
set "PROPERTIES_FILE=%BASEDIR%.mvn\wrapper\maven-wrapper.properties"
set "MAVEN_PROJECTBASEDIR=%BASEDIR%"
if not "%JAVA_HOME%"=="" (
  set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVA_CMD=java.exe"
)
"%JAVA_CMD%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
