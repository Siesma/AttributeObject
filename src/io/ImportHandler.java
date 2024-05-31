package type.io;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Stack;

public class ImportHandler implements ImportHelper {

    private String fullDataString;
    private String changedDataString;
    private final Stack<Character> specialSigns;
    private ArrayList<Character> specialChars;
    private ArrayList<Character> whiteSpaceChars;

    public ImportHandler() {
        this.specialSigns = new Stack<>();
        adjustSpecialChars(':', '\n', ',');
        adjustWhiteSpaceChars(' ', '\t');
    }

    public <T extends ImportableAttrObject<?>> T createFromFile(String fileName, Class<T> clazz) {
        loadFile(fileName);
        String identifier = read();
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            T obj = constructor.newInstance(identifier);
            obj.readFromFile(this);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void loadFile(String pathToFile) {
        this.fullDataString = readContentOfFile(pathToFile);
        this.changedDataString = fullDataString;
    }
    @Override
    public int getDepth() {
        return specialSigns.size();
    }

    @Override
    public String read() {
        consumeWhiteSpaces();
        char c;
        StringBuilder builder = new StringBuilder();
        while (!changedDataString.isEmpty()) {
            if (isSpecialSign(c = consume())) {
                while (!changedDataString.isEmpty() && isSpecialSign(peak())) {
                    consume();
                }
                break;
            }
            consumeWhiteSpaces();
            builder.append(c);
        }
        return builder.toString();
    }

    private void adjustSpecialChars(Character... chars) {
        if (specialChars == null) {
            this.specialChars = new ArrayList<>();
        }
        for (Character c : chars) {
            if (!specialChars.contains(c)) {
                specialChars.add(c);
            }
        }
    }
    private void adjustWhiteSpaceChars(Character... chars) {
        if (whiteSpaceChars == null) {
            this.whiteSpaceChars = new ArrayList<>();
        }
        for (Character c : chars) {
            if(c == '{' || c == '}') {
                System.out.println("\"{\" and \"}\" have special push and pop operations, they can not be implicit special chars");
                continue;
            }
            if (!whiteSpaceChars.contains(c)) {
                whiteSpaceChars.add(c);
            }
        }
    }
    private boolean isSpecialSign(char c) {
        if (c == '{') {
            specialSigns.push(c);
            return true;
        }
        if (c == '}') {
            specialSigns.pop();
            return true;
        }
        for (Character character : specialChars) {
            if (c == character) {
                return true;
            }
        }
        return false;
    }

    public String getChangedDataString () {
        return changedDataString;
    }

    private void consumeWhiteSpaces() {
        if (changedDataString.isEmpty()) {
            return;
        }
        while (isWhiteSpace(changedDataString.charAt(0))) {
            changedDataString = changedDataString.substring(1);
        }
    }
    private boolean isWhiteSpace(char c) {
        for (Character character : whiteSpaceChars) {
            if (c == character) {
                return true;
            }
        }
        return false;
    }
    private char peak() {
        return changedDataString.charAt(0);
    }
    private char consume() {
        char firstChar = changedDataString.charAt(0);
        changedDataString = changedDataString.substring(1);
        return firstChar;
    }
}

