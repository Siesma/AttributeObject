package type;

import type.attr.Attribute;
import type.attr.AttributeIdentifier;
import type.attr.Identifier;

import java.lang.reflect.ParameterizedType;
import java.util.*;

public interface AttrObject<T> {

    HashMap<String, AttrObject<T>> getFieldMap();

    HashMap<Identifier, Attribute<T>> getAttributeMap();

    HashMap<String, Identifier> getIdentifierMap();

    final String VARIANT_INDICATOR = "Variants";

    default void init(Attribute<T>... attributes) {
        addAttribute(attributes);
    }

    default AttributeIdentifier<T> hasGivenAttribute(String lookup) {
        Identifier identifier = obtainIdentifier(lookup);
        if (getAttributeMap().containsKey(identifier)) {
            return new AttributeIdentifier<>(true, getAttributeMap().get(identifier));
        }
        return new AttributeIdentifier<>(false, null);
    }

    default boolean hasField(String name) {
        return getFieldMap().containsKey(name);
    }

    default boolean hasVariants() {
        return hasField(VARIANT_INDICATOR);
    }

    default AttrObject<T> getVariantDifferences(String name) {
        if (!hasField(VARIANT_INDICATOR)) {
            return null;
        }
        AttrObject<T> variants = getFieldMap().get(VARIANT_INDICATOR);
        return variants.getFieldMap().get(name);
    }


    default Collection<AttrObject<T>> getVariants() {
        if (!hasField(VARIANT_INDICATOR)) {
            return null;
        }
        return getFieldMap().get(VARIANT_INDICATOR).getFieldMap().values();
    }

    default String getLookupName() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            return clazz.getSimpleName();
        } catch (Exception e) {
            // class is not parameterized
            return this.getClass().getSimpleName();
        }
    }

    T fromString(String value);

    default void addAttribute(Attribute<T> attribute) {
        if (checkIfAttributeExists(attribute.getLookup())) {
            return;
        }
        getAttributeMap().put(obtainIdentifier(attribute.getLookup()), attribute);
    }

    default void addAttribute(Attribute<T>... attributes) {
        Arrays.stream(attributes).forEach(this::addAttribute);
    }

    default void removeAttribute(String lookup) {
        Identifier identifier = obtainIdentifier(lookup);
        getIdentifierMap().remove(lookup);
        this.getAttributeMap().remove(identifier);
    }

    default boolean checkIfAttributeExists(String s) {
        return getAttributeMap().containsKey(obtainIdentifier(s));
    }

    default Identifier obtainIdentifier(String s) {
        if (getIdentifierMap().containsKey(s)) {
            return getIdentifierMap().get(s);
        }
        Identifier identifier = new Identifier(s);
        getIdentifierMap().put(s, identifier);
        return identifier;
    }

    private <K, V> String getKeyByValue(HashMap<String, AttrObject<T>> map, AttrObject<T> value) {
        for (Map.Entry<String, AttrObject<T>> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    default String asString() {
        StringBuilder builder = new StringBuilder();
        if (getLookupName().equalsIgnoreCase("")) {
            AttributeIdentifier<T> nameIdentifier = hasGivenAttribute("Name");
            if(nameIdentifier.doesExist()) {
                builder.append(nameIdentifier.getAttribute().getData()).append(" ");
            }
            builder.append("{\n");
        } else {
            builder.append(getLookupName()).append(" {\n");
        }
        for (AttrObject<T> field : getFieldMap().values()) {
            String key = getKeyByValue(getFieldMap(), field);
            if (key == null) {
                // should never occur
                continue;
            }
            String fieldString = field.asString().replaceAll("\n", "\n\t").substring(field.getLookupName().length());
            builder.append("\t").append(key).append(fieldString).append("\n");
        }
        for (Attribute<T> attribute : getAttributeMap().values()) {
            builder.append(String.format("\t%s: %s\n", attribute.getLookup(), attribute.getData()));
        }
        builder.append("}");
        return builder.toString();

    }
}
