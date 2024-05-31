package type;

import type.attr.Attribute;
import type.io.ImportHandler;
import type.io.ImportableStringObject;

public class main {

    public static void main(String[] args) {
        ImportHandler handler = new ImportHandler();

        ImportableStringObject other = handler.createFromFile("Data", ImportableStringObject.class);

        System.out.println(other.toString());

    }

}
