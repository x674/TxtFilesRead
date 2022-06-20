import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws IOException {
        String workingDir;
        if (args.length > 0) {
            workingDir = String.valueOf(args[0]);
        }
        else
        {
            workingDir = System.getProperty("user.dir");
        }
        Path pathWorkingDir = Paths.get(workingDir);
        Path outputFilePath = Paths.get(workingDir, "out.txt");
        if (Files.exists(outputFilePath)) {
            Files.delete(outputFilePath);
        }

        var result = findByFileExtension(pathWorkingDir, ".txt");
        String outputString = result.stream()
                .sorted((currentFile, nextfile) -> currentFile.getFileName().compareTo(nextfile.getFileName()))
                .map(file -> {
                    try {
                        return Files.readAllLines(file).stream().collect(Collectors.joining(""));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.joining(""));
        
        Files.write(outputFilePath, outputString.getBytes());
    }

    public static List<Path> findByFileExtension(Path path, String extension)
            throws IOException {

        List<Path> result;
        try (Stream<Path> pathStream = Files.find(path,
                Integer.MAX_VALUE,
                (p, basicFileAttributes) -> p.getFileName().toString().endsWith(extension))) {
            result = pathStream.collect(Collectors.toList());
        }
        return result;
    }
}
