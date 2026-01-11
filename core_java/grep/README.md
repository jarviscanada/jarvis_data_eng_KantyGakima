# Introduction
The Java Grep application is a command-line tool designed to recursively search files within a directory and extract lines 
that match a given regular expression. It efficiently processes large text datasets and writes matching results to an output file.

The project is built using several core technologies:

* `Core Java` for portability and file I/O operations.
* `Java Streams` and Lambda expressions for concise data processing
* Regular Expressions (`Regex`) for  pattern matching
* Libraries like `SLF4J` with `Log4j` for structured logging
* `Maven` for dependency management and packaging
* `Docker` for containerized execution

# Quick Start

**Build the application**\
Build the project and generate a runnable fat JAR using Maven:

```
mvn clean package
```
**Run locally**
```
java -jar target/grep-1.0-SNAPSHOT.jar \
".*Romeo.*Juliet.*" <root_directory> <output_file>
```
Example: \
This command searches foa regex pattern in /data directory and output the matching lines in out/grep_out.txt 
```
java -jar target/grep-1.0-SNAPSHOT.jar \
".*Romeo.*Juliet.*" ./data out/grep_out.txt
```
**Run using docker**\
Docker allows the application to run without installing Java or Maven locally.

Build the Docker image 
```
docker build -t java-grep . 
```
*Note*: A Docker Hub account is not required unless you plan to push the image to a remote registry.

Run the container:
```
docker run --rm \
-v "$(pwd)/data:/data" \
-v "$(pwd)/log:/log" \
java-grep \
".*Romeo.*Juliet.*" /data /log/grep.out
```
# Implementation
## Pseudocode
```
Process():
    matchedLines = []
    for file in listFiles(rootDir):
      for line in readLines(file)
          if containsPattern(line)
            matchedLines.add(line)
    writeToFile(matchedLines)
```

## Performance Issue
The main performance concern is memory usage, as matched lines are accumulated in matchedlines list before being written
to disk. For a large datasets, this can lead to high memory consumption.This issue can be improved by writing matched lines
incrementally to the output file instead of storing them in a list.


# Test
The application was tested manually by:
* Preparing sample text files with known matching and non-matching lines in `/data` directory
* Running the application with different regex patterns
* Verifying the output file contents against expected results
* Testing edge cases such as empty directories, invalid paths and not matches
* Running the application on multiple directory structures to validate recursive traversal
# Deployment
This project is containerized with Docker for consistent execution across environments.
## Docker packaging : Dockerfile
- Uses a lightweight runtime image (JRE)
- Copies the fat JAR into the container
- Runs the application via `java -jar`

## Optional: Push the image to Docker Hub
Only required if you want to distribute the image publicly.

```bash
#Register Docker hub account
docker_user=your_docker_id
docker login -u ${docker_user}  
#build a new docker image locally
docker build -t ${docker_user}/grep .
#Run docker container
docker run --rm \
-v `pwd`/data:/data -v `pwd`/log:/log \
${docker_user}/grep ".*Romeo.*Juliet.*" /data /log/grep.out

#push your image to Docker Hub
docker push ${docker_user}/grep
```


# Improvement
* Stream matched lines directly to the output file to reduce memory usage.
* Add automated tests for regex matching, file traversal and output generation JUnit.
* Use parallel file processing so multiple files can be scanned concurrently, improving performance on large directory trees.