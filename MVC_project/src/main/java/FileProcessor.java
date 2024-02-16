import java.io.*;
import java.util.*;

public class FileProcessor {

    // Uncomment if keeping vehicles from other sessions is desired
//    public List<String> readFromFile(String filePath) throws BusinessException {
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//
//            // Initialize a list to store lines read from the file
//            List<String> lines = new ArrayList<>();
//            String line;
//
//            // Read each line from the file and add it to the list
//            while ((line = reader.readLine()) != null) {
//                lines.add(line);
//            }
//
//            // Return the list of lines read from the file
//            return lines;
//
//        } catch (IOException e) {
//            // If an IOException occurs during file reading, wrap it in a BusinessException and throw
//            throw new BusinessException(e.getMessage(), "readFromFile");
//        }
//    }

    public void writeToFile(String filePath, List<String> lines) throws BusinessException {

        // Create a File object using the specified file path
        File file = new File(filePath);

        // Get the parent directory of the file
        File parentDirectory = file.getParentFile();

        // Check if the parent directory does not exist
        if (!parentDirectory.exists()) {

            // Attempt to create parent directories
            boolean directoriesCreated = parentDirectory.mkdirs();

            // Check if directories were created successfully
            if (!directoriesCreated) {
                // Handle failure to create directories
                throw new BusinessException("Failed to create parent directories.", "writeToFile");
            }
        }

        // Initialize a PrintWriter to write lines to the file
        // Iterate through the list of lines and write each line to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.println(line);
            }

        } catch (IOException e) {
            // If an IOException occurs during file writing, wrap it in a BusinessException and throw
            throw new BusinessException(e.getMessage(), "writeToFile");
        }
    }
}