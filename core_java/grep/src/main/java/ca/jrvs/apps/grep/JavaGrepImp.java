package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep{

    final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String regex;
    private String rootPath;
    private String outFile;
    private Pattern pattern;

    @Override
    public void process() throws IOException {
        logger.info("Starting process. regex={}, rootPath={}, outFile={}",
                regex, rootPath, outFile);
        //compile regex once
        this.pattern = Pattern.compile(regex);

        List<String> matchedLines = new ArrayList<>();

        for (File file : listFiles(rootPath)){
            for (String line : readLines(file)){
                if (containsPattern(line)){
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
        logger.info("Process completed. Matched {} lines.", matchedLines.size());
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> files = new ArrayList<>();
        File root = new File(rootDir);
        if (!root.exists()){
            logger.warn("Root path does not exist: {}", rootDir);
            return files;
        }
        if(root.isFile()){
            files.add(root);
            return files;
        }

        for (File f : Objects.requireNonNull(root.listFiles())){
            if(f.isDirectory()) {
                files.addAll(listFiles(f.getAbsolutePath()));
            } else {
                files.add(f);
            }
        }

        return files;

    }

    @Override
    public List<String> readLines(File inputFile) throws IOException {

        if (!inputFile.isFile()) {
            throw new IllegalArgumentException("Not a file: " + inputFile.getPath());
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }


    @Override
    public boolean containsPattern(String line) {
        if(pattern == null){
            //compile regex if this function is called without process()
            pattern = Pattern.compile(regex);
        }

        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    @Override
    public void writeToFile(List<String> lines) throws IOException {

        File out = new File(outFile);
        //ensure parent directory exists
        File parent = out.getParentFile();
        if (parent != null && !parent.exists()){
            boolean dirCreated = parent.mkdirs();
            if(!dirCreated){
                logger.warn("Could not create parent directory: {}", parent.getAbsolutePath());
            }
        }
        //ensure file exists
        if(!out.exists()){
            boolean fileCreated = out.createNewFile();
            if(!fileCreated){
                logger.warn("Could not create output file: {}", out.getAbsolutePath());
            }
        }
        //write content
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(Files.newOutputStream(out.toPath()), StandardCharsets.UTF_8))) {

            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }


    //getters and setters

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public static void main(String[] args) {
        if(args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrepImp regex rootpath outFile");
        }

        //use default logger config
        BasicConfigurator.configure();

        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try{
            javaGrepImp.process();
        } catch (Exception ex) {
            javaGrepImp.logger.error("Error: Unable to process", ex);
        }
    }
}
