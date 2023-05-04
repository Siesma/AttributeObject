package type;

import type.attr.Attribute;
import type.attr.AttributeIdentifier;
import type.attr.Identifier;

import java.util.Arrays;
import java.util.HashMap;

public abstract class AttributeObject<T> {
    protected HashMap<Identifier, Attribute<T>> attributeMap;

    protected HashMap<String, Identifier> identifierMap;

    @SafeVarargs
    public AttributeObject(Attribute<T>... attributes) {
        init(attributes);
    }

    @SafeVarargs
    public final void init(Attribute<T>... attributes) {
        attributeMap = new HashMap<>();
        this.identifierMap = new HashMap<>();
        addAttribute(attributes);
    }

    public final AttributeIdentifier<T> hasGivenAttribute(String lookup) {
        Identifier identifier = obtainIdentifier(lookup);
        if (attributeMap.containsKey(identifier)) {
            return new AttributeIdentifier<>(true, attributeMap.get(identifier));
        }


        return new AttributeIdentifier<>(false, null);
    }

    public final void addAttribute(Attribute<T> attribute) {
        if (checkForExistence(attribute.getLookup())) {
            return;
        }
        attributeMap.put(obtainIdentifier(attribute.getLookup()), attribute);
    }

    @SafeVarargs
    public final void addAttribute(Attribute<T>... attributes) {
        Arrays.stream(attributes).forEach(this::addAttribute);
    }

    public final void removeAttribute (String lookup) {
        Identifier identifier = obtainIdentifier(lookup);
        identifierMap.remove(lookup);
        this.attributeMap.remove(identifier);
    }

    private boolean checkForExistence(String s) {
        return attributeMap.containsKey(obtainIdentifier(s));
    }

    private Identifier obtainIdentifier(String s) {
        if (identifierMap.containsKey(s)) {
            return identifierMap.get(s);
        }
        Identifier identifier = new Identifier(s);
        identifierMap.put(s, identifier);
        return identifier;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getSimpleName()).append(" {\n");
        for (Attribute<T> attribute : attributeMap.values()) {
            builder.append(String.format("\t%s: %s\n", attribute.getLookup(), attribute.getData()));
        }
        builder.append("}");
        return builder.toString();

    }
}
