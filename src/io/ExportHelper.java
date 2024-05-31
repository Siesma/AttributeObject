package type.io;

import java.util.Arrays;

interface ExportHelper {

    void createFile(String pathToFile, String fileName);

    void fillFile(String pathToFile, String fileName, String data);

    default void fillFileWithObjects (String pathToFile, String fileName, Object... objects) {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(objects).forEach(builder::append);
        fillFile(pathToFile, fileName, builder.toString());
    }

    default void appendToFile (String pathToFile, String fileName, String append) {

    }

    private String getDir () {
        return System.getenv("user.dir");
    }

}
