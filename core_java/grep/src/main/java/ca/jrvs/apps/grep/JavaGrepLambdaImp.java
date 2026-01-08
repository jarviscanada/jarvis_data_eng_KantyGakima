package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp{

    public static void main(String[] args) {


        final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);

        if (args.length !=3) {
            throw new IllegalArgumentException("USAGE: JavaGrepLambdaImp regex rootpath outFile");
        }

        //use default logger config
        BasicConfigurator.configure();

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try {
            javaGrepLambdaImp.process();
        } catch (Exception ex) {
            javaGrepLambdaImp.logger.error("Error: Unable to process", ex);
        }
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void process() throws IOException {
        logger.info("Starting process. regex={}, rootPath={}, outFile={}",
                getRegex(), getRootPath(), getOutFile());
        //compile regex
        this.pattern = Pattern.compile(getRegex());
        List<String> matchedLines =
                listFiles(getRootPath()).stream()
                        .flatMap(file -> {
                            try {
                                return readLines(file).stream();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .filter(this::containsPattern)
                        .collect(Collectors.toList());
        writeToFile(matchedLines);
        logger.info("Process completed. Matched {} lines.", matchedLines.size());
    }

    /**
     *
     * @param inputFile
     * @return lines as a list
     */
    @Override
    public List<String> readLines(File inputFile) throws IOException {
        if (!inputFile.isFile()){
            throw new IllegalArgumentException("Not a file " + inputFile.getPath());
        }
        try(Stream<String> lines = Files.lines(inputFile.toPath())) {
            return lines.collect(Collectors.toList());
        }
    }

    /**
     *
     * @param rootDir input directory
     * @return files under rootDir
     */

    @Override
    public List<File> listFiles(String rootDir) {
        Path root = Paths.get(rootDir);

        if (!Files.exists(root)){
            logger.warn("Root path does not exist: {}", rootDir);
            return List.of();
        }
        try(Stream<Path> paths = Files.walk(root)) {
            return  paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e){
            throw new RuntimeException("Error walking file tree: " + rootDir, e);
        }
    }
}

