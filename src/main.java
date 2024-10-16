package type;

import type.attr.Attribute;
import type.io.ImportHandler;
import type.io.ImportableStringObject;

import java.io.BufferedReader;
import java.io.StringReader;

public class main {

    public static void main(String[] args) {
        ImportHandler handler = new ImportHandler();

        // Load the object from the file
        ImportableStringObject other = handler.createFromFile("Data", ImportableStringObject.class);

        if (other == null) {
            return;
        }
        String serializedData = other.toString();
        System.out.println("Serialized data: ");
        System.out.println(serializedData);

        BufferedReader stream = new BufferedReader(new StringReader(serializedData));

        ImportableStringObject deserializedObject = handler.createFromStream(stream, ImportableStringObject.class);

        System.out.println("Deserialized object:\n" + deserializedObject);

        System.out.println("Failed to load object from file.");

    }

}
