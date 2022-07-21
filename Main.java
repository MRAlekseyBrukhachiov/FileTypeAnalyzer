package analyzer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    private static List<FilePattern> generateListOfFilePatterns(File datafile) throws IOException {
        List<FilePattern> list = new ArrayList<>();
        List<String> lines = Files.readAllLines(datafile.toPath());
        for (String line: lines) {
            String[] fields = line.split(";");
            FilePattern fp = new FilePattern(Integer.parseInt(fields[0]),
                    fields[1].replace("\"",""),
                    fields[2].replace("\"",""));
            list.add(fp);
        }
        return list;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        File root = new File(args[0]);
        String PATTERNS_PATH = args[1];

        File[] files = root.listFiles();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<FilePattern> listOfFilePatterns = generateListOfFilePatterns(new File(PATTERNS_PATH));

        assert files != null;
        for (File file : files) {
            executor.execute(new Processor(file, listOfFilePatterns));
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }

        executor.shutdown();
    }
}
