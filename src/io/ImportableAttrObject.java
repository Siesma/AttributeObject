package type.io;

import jdk.jfr.Label;
import type.AttrObject;
import type.attr.Attribute;

public abstract class ImportableAttrObject<T> extends AttrObject<T> {

    public ImportableAttrObject(String identifier, Attribute<T>... attributes) {
        super(identifier, attributes);
    }
    public ImportableAttrObject(String identifier) {
        super(identifier);
    }

    public void read(ImportHelper importHelper) {
        int depth = importHelper.getDepth();
        String cur;
        while (depth == importHelper.getDepth() && !(cur = importHelper.read()).equalsIgnoreCase("") /*means that the object is done*/) {
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
    protected AttrObject<T> readField(ImportHelper importHelper, String identifier) {
        ImportableAttrObject<T> val = createNew(identifier);
        val.read(importHelper);
        return val;
    }

    protected static <T> ImportableAttrObject<T> createNew(String identifier) {
        ImportableAttrObject<T> val = new ImportableAttrObject<T>(identifier) {
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

}
