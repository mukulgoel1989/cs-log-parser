a simple spring boot application that takes in a log file, parses, collates and saves the events to a file based hsqldb

# Instructions to build the application

* Download git repository
* Navigate to downloaded repository and execute `mvn clean install`
* Executable jar is generated to `target/log-parser-1.0.0.jar`

## Notes

* Note the pathToLogFile parameter is mandatory. This can be either absolute or relative path to file
* The application will start and read log file from provided path. If a valid file is found, the application will parse
  the log events and save EventDetails to a file based HSQL database. The database file is configured to be
  called `eventdb` and will be placed in a subdirectory `db` in working directory

# Instructions to run application from command line

* Navigate to where log-parser-1.0.0.jar is on the system
* Execute `java -jar log-parser-1.0.0.jar --app.logfile.path=<pathToLogFile>`
* Alternatively you can execute the jar from anywhere on system by prd oviding full path to jar file instead
* Execute `java -jar <Path-To-JAR-File>/log-parser-1.0.0.jar --app.logfile.path=<pathToLogFile>`
