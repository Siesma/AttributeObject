package type.io;

import type.attr.Attribute;

public class ImportableStringObject extends ImportableAttrObject<String> {
    public ImportableStringObject(String identifier, Attribute<String>... attributes) {
        super(identifier, attributes);
    }

    public ImportableStringObject(String identifier) {
        super(identifier);
    }

    public ImportableStringObject (ImportableAttrObject<?> other) {
        super(other.getOwnIdentifier());
    }

    @Override
    protected String fromString(String value) {
        return value;
    }
}
