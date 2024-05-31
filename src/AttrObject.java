package type;

import type.attr.Attribute;
import type.attr.AttributeIdentifier;
import type.attr.Identifier;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Interface to implement a Datatype that has attributes and other of the same Datatype attached to it
 *
 * @param <T>
 */
public abstract class AttrObject<T> {


    private HashMap<String, AttrObject<T>> fieldMap;
    private HashMap<Identifier, Attribute<T>> attributeMap;
    private HashMap<String, Identifier> identifierMap;

    private String ownIdentifier;


    public AttrObject (String identifier, Attribute<T>... attributes) {
        this.ownIdentifier = identifier;
        init(attributes);
    }

    /**
     * A field is a recursive relation, a field represents another AttrObject.
     * @return Map containing fields.
     */
    protected HashMap<String, AttrObject<T>> getFieldMap() {
        return fieldMap;
    }

    /**
     * An attribute is a qualifier within a field, a simple "ID:Attribute" pair
     * @return Map containing all attributes of this field.
     */
    protected HashMap<Identifier, Attribute<T>> getAttributeMap() {
        return attributeMap;
    }

    /**
     * An Identifier is a qualifier within a field, a simple "ID:Attribute" pair
     * @return Map containing all identifiers of this field.
     */
    protected HashMap<String, Identifier> getIdentifierMap() {
        return identifierMap;
    }


    /**
     * Attempts to parse a String to type T
     *
     * @param value that has to be parsed
     * @return a new Object of type T
     */
    protected abstract T fromString(String value);


    /**
     * @return the identifier that correlates to this object
     */
    public String getOwnIdentifier() {
        return ownIdentifier;
    }

    private final String VARIANT_INDICATOR = "Variants";

    /**
     * Initializes the attributeMap and
     *
     * @param attributes
     */
    private void init(Attribute<T>... attributes) {
        this.fieldMap = new HashMap<>();
        this.identifierMap = new HashMap<>();
        this.attributeMap = new HashMap<>();
        addAttribute(attributes);
    }

    /**
     * Check if the AttrObject has a given Attribute given a lookup string
     * This implementation is a relict from an old project, does not require an AttributeIdentifier but can just return the Attribute itself
     *
     * @param lookup string how to find the Attribute in question
     * @return an AttributeIdentifier which has the exists flag set if and only if the attribute is not null
     */
    public AttributeIdentifier<T> hasGivenAttribute(String lookup) {
        Identifier identifier = obtainIdentifier(lookup);
        if (getAttributeMap().containsKey(identifier)) {
            return new AttributeIdentifier<>(getAttributeMap().get(identifier));
        }
        return new AttributeIdentifier<>(null);
    }

    /**
     * Check if the AttrObject holds data for an additional AttrObject
     *
     * @param name of the potential other AttrObject
     * @return whether the AttrObject exists
     */
    public boolean hasField(String name) {
        return getFieldMap().containsKey(name);
    }

    /**
     * Check if the AttrObject has so-called "Variants".
     * Variants are the same AttrObject but with slightly different Attributes.
     * They are indicated by "Variants { NameOfVariant1 { ... } NameOfVariant2 { ... } ...}
     *
     * @return whether the AttrObject has variants
     */
    public boolean hasVariants() {
        return hasField(VARIANT_INDICATOR);
    }

    /**
     * Returns the AttrObject of the specified Variant
     *
     * @param name of the variant
     * @return the AttrObject that refers to the Variant
     */
    public AttrObject<T> getVariantDifferences(String name) {
        if (!hasField(VARIANT_INDICATOR)) {
            return null;
        }
        AttrObject<T> variants = getFieldMap().get(VARIANT_INDICATOR);
        return variants.getFieldMap().get(name);
    }

    /**
     * @return Collection of Variants
     */
    public Collection<AttrObject<T>> getVariants() {
        if (!hasField(VARIANT_INDICATOR)) {
            return null;
        }
        return getFieldMap().get(VARIANT_INDICATOR).getFieldMap().values();
    }

    /**
     * @return String that tries to be the Generic Datatype, if the AttrObject is not initialized it is just the class name
     * Could also be interpreted as "the generic datatypes type"
     */
    public String getLookupName() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            return clazz.getSimpleName();
        } catch (Exception e) {
            // class is not parameterized
            return this.getClass().getSimpleName();
        }
    }


    /**
     * Primary way of getting an attribute from an identifier
     * @param lookup Attribute's ID pair
     * @return The attribute.
     * Default return value is an Attribute with no Lookup and data being null
     */
    public Attribute<T> getAttribute(String lookup) {
        Identifier identifier = obtainIdentifier(lookup);
        return getAttributeMap().get(identifier);
    }

    /**
     * Primary way of accessing the field of an AttrObject
     * @param lookup of the field
     * @return The given field. Returns null if the lookup is not defined
     */
    AttrObject<T> getField(String lookup) {
        return getFieldMap().getOrDefault(lookup, null);
    }

    /**
     * Primary way of adding an attribute to the list of Attributes
     *
     * @param attribute that shall be added, does nothing if the Attribute already existed
     */
    public void addAttribute(Attribute<T> attribute) {
        if (checkIfAttributeExists(attribute.lookup())) {
            return;
        }
        getAttributeMap().put(obtainIdentifier(attribute.lookup()), attribute);
    }

    /**
     * Finds a field given by a name
     *
     * @param fieldName of the field
     * @return AttrObject that refers to the field with name fieldName
     */
    public AttrObject<T> recursiveFieldSearch(String fieldName) {
        if (getFieldMap().isEmpty()) {
            return null;
        }
        if (getFieldMap().containsKey(fieldName)) {
            return getFieldMap().get(fieldName);
        }
        for (AttrObject<T> obj : getFieldMap().values()) {
            AttrObject<T> temp = obj.recursiveFieldSearch(fieldName);
            if (temp != null) {
                return temp;
            }
        }
        return null;
    }


    /**
     * Gives a list of all AttrObjects that have a lookup for an Attribute
     *
     * @param attributeName that has to be filtered for
     * @return list of all AttrObjects with an Attribute
     */
    public List<AttrObject<T>> recursiveAttributeSearch(String attributeName) {
        List<AttrObject<T>> attrObjects = new ArrayList<>();
        AttributeIdentifier<T> attributeIdentifier = hasGivenAttribute(attributeName);
        if (attributeIdentifier.doesExist()) {
            attrObjects.add(this);
        }
        getFieldMap().values().forEach(e -> attrObjects.addAll(e.recursiveAttributeSearch(attributeName)));
        return attrObjects;
    }

    /**
     * Overloaded addAttribute method for multiple Attributes
     *
     * @param attributes to be added
     */
    public void addAttribute(Attribute<T>... attributes) {
        Arrays.stream(attributes).forEach(this::addAttribute);
    }

    /**
     * Removes an Attribute
     *
     * @param lookup of the Attribute that has to be removed
     */
    public void removeAttribute(String lookup) {
        Identifier identifier = obtainIdentifier(lookup);
        getIdentifierMap().remove(lookup);
        this.getAttributeMap().remove(identifier);
    }

    /**
     * Checks if an attribute Exists
     *
     * @param lookup string of the Attribute
     * @return whether the Attribute exists or not
     */
    public boolean checkIfAttributeExists(String lookup) {
        return getAttributeMap().containsKey(obtainIdentifier(lookup));
    }

    /**
     * Obtains the Lookup Identifier of an Attribute
     *
     * @param lookup string of the Attribute
     * @return the Identifier for the Attribute
     */
    Identifier obtainIdentifier(String lookup) {
        if (getIdentifierMap().containsKey(lookup)) {
            return getIdentifierMap().get(lookup);
        }
        Identifier identifier = new Identifier(lookup);
        getIdentifierMap().put(lookup, identifier);
        return identifier;
    }

    /**
     * @return the entire AttrObject into a String
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getOwnIdentifier()).append(" {\n");
        // print the name as the first Attribute
        if (hasGivenAttribute("Name").doesExist()) {
            builder.append(hasGivenAttribute("Name").getAttribute());
        }
        for (Attribute<?> attr : getAttributeMap().values()) {
            // dont print the name twice
            if (attr.lookup().equals("Name")) {
                continue;
            }
            builder.append(attr);
        }
        // alternative way, if the name is not first to be printed
//        getAttributeMap().values().forEach(builder::append);
        for (AttrObject<T> field : getFieldMap().values()) {
            // if the own identifier is not supposed to be printed, this is what should be used
//            String fieldString = field.asString().replaceAll("\n", "\n\t").substring(field.getOwnIdentifier().length());
            String fieldString = field.toString().replaceAll("\n", "\n\t");
            builder.append("\t").append(fieldString).append("\n");
        }
        builder.append("}");
        return builder.toString();

    }
}
