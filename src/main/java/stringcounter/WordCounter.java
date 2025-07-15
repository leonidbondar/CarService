package stringcounter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class WordCounter {
    private static final Logger log = LogManager.getLogger(WordCounter.class);

    /**
     * Reads the entire inputFile, counts occurrences of each searchTerm
     * (case‐insensitive), and appends a timestamped report to outputFile.
     */
    public static void countWords(File inputFile, File outputFile, List<String> searchTerms) {
        try {
            // 1. Read the entire file into a string
            String content = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
            String lower = content.toLowerCase();

            // 2. Build the report
            StringBuilder report = new StringBuilder()
                    .append("---- Word Count Report ----\n")
                    .append("Timestamp : ")
                    .append(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .append("\n")
                    .append("Source    : ")
                    .append(inputFile.getAbsolutePath())
                    .append("\n\n");

            for (String term : searchTerms) {
                int count = StringUtils.countMatches(lower, term.toLowerCase());
                report.append(String.format("%-20s : %d%n", term, count));
            }
            report.append("----------------------------\n\n");

            // 3. Append to outputFile
            FileUtils.writeStringToFile(outputFile, report.toString(), StandardCharsets.UTF_8, true);

            log.info("Report appended to {}", outputFile.getName());
        } catch (IOException e) {
            log.error("Failed to process '{}': {}", inputFile.getAbsolutePath(), e.getMessage(), e);
        }
    }

    /**
     * Usage:
     * java -cp <your-jar-and-libs> stringcounter.WordCounter
     * <inputFile> <outputFile> <term1> [<term2> ...]
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            log.error("Usage: java -cp <classpath> stringcounter.WordCounter <inputFile> <outputFile> <term1> [<term2> ...]");
            System.exit(1);
        }

        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        List<String> terms = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));

        log.info("Starting word count on '{}' for terms {}", inputFile.getName(), terms);
        countWords(inputFile, outputFile, terms);
        log.info("Word count finished.");
    }
}
