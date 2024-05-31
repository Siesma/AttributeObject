package type.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public interface ImportHelper {

    /**
     * Requires an internal field to save the data
     * @param pathToFile
     */
    void loadFile(String pathToFile);

    /**
     * With each "{" the depth increases, and each "}" decreases the depth
     * @return amount of opened (and not closed) "{"
     */
    int getDepth();

    /**
     * Reads until a special symbol is found
     * @return string that just been read
     */
    String read();
    default String readContentOfFile (String fileName) {
        String DIRECTORY = System.getProperty("user.dir");
        StringBuilder stringBuilder = new StringBuilder();
        String path = DIRECTORY + "/" + fileName;
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (Exception e) {
                System.err.println("Couldnt create file, exiting");
                System.exit(1);
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException fnfe) {
            fnfe.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
