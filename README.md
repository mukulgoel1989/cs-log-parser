a simple spring boot application that takes in a log file, parses, collates and saves the events to a file based hsqldb

# Instructions to build the application

* Download git repository
* Navigate to downloaded repository and execute `mvn clean install`
* Executable jar is generated to `target/log-parser-1.0.0.jar`

## Notes

* Application developed and tested with JDK8
* Note the pathToLogFile parameter is mandatory. This can be either absolute or relative path to file
* The application will start and read log file from provided path. If a valid file is found, the application will parse
  the log events and save EventDetails to a file based HSQL database. The database file is configured to be
  called `eventdb` and will be placed in a subdirectory `db` in working directory

# Instructions to run application from command line

* Navigate to where log-parser-1.0.0.jar is on the system
* Execute `java -jar log-parser-1.0.0.jar --app.logfile.path=<pathToLogFile>`
* Alternatively you can execute the jar from anywhere on system by prd oviding full path to jar file instead
* Execute `java -jar <Path-To-JAR-File>/log-parser-1.0.0.jar --app.logfile.path=<pathToLogFile>`

# Notes for improvement

* From the problem statement there is a use case that indicates towards very large files (in GBs)
    * I have used a stream implementation of file reader to not read the full file in memory before processing
    * Also configured property to instruct spring jpa to persist in batches instead of one big flush.

* But the program still builds the full list of JSONObjects before processing and persisting to database. This could put
  a strain and even throw outOfMemory if the input file is way too big. If the file can be too big where it can make the
  program go out of memory, we can implement a chunk based processing system here like spring batch which would read and
  process the file in chunks and reducing memory load