package type.io;

import jdk.jfr.Label;
import type.AttrObject;
import type.attr.Attribute;
import type.attr.Identifier;

import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

public interface ImportableAttrObject<T> extends AttrObject<T> {

    default void readFromFile(ImportHelper importHelper) {
        int depth = importHelper.getDepth();
        String cur;
        while (depth == importHelper.getDepth() && !(cur = importHelper.read()).equalsIgnoreCase("")) {
            if (depth < importHelper.getDepth()) {
                // Nested object
                AttrObject<T> field = readField(importHelper, cur);
                getFieldMap().put(cur, field);
            } else {
                // Direct attribute
                String value = importHelper.read();
                T var = fromString(value);
                Attribute<T> attr = new Attribute<>(cur, var);
                addAttribute(attr);
            }
        }
    }

    default AttrObject<T> readField(ImportHelper importHelper, String identifier) {
        ImportableAttrObject<T> val = createNew(identifier);
        val.readFromFile(importHelper);
        return val;
    }

    default ImportableAttrObject<T> createNew(String identifier) {
        HashMap<Identifier, Attribute<T>> attrMap = new HashMap<>();
        HashMap<String, Identifier> idenMap = new HashMap<>();
        HashMap<String, AttrObject<T>> fieldMap = new HashMap<>();

        ImportableAttrObject<T> val = new ImportableAttrObject<T>() {
            @Override
            public HashMap<String, AttrObject<T>> getFieldMap() {
                return fieldMap;
            }

            @Override
            public HashMap<Identifier, Attribute<T>> getAttributeMap() {
                return attrMap;
            }

            @Override
            public HashMap<String, Identifier> getIdentifierMap() {
                return idenMap;
            }

            @Override
            public String getOwnIdentifier() {
                return identifier;
            }

            @Override
            public T fromString(String s) {
                // TODO: Figure out a way to parse a string to an object of type T
//                String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
//                try {
//                    Class<?> clazz = Class.forName(className);
//                    String mySimpleName = simpleClassName(clazz);
//                } catch (Exception e){}
                return (T) s;
            }
        };
        return val;
    }

    @Label("Unused")
    private String simpleClassName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    default void appendToFile(ExportHelper exportHelper) {

    }

}
