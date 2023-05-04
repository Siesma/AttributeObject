package type.attr;

public class AttributeIdentifier<T> {
    private boolean exists;
    private Attribute<T> attribute;

    public AttributeIdentifier(boolean exists, Attribute<T> attribute) {
        this.exists = exists;
        this.attribute = attribute;
    }

    public Attribute<T> getAttribute() {
        return attribute;
    }

    public AttributeIdentifier<T> setAttribute(Attribute<T> attribute) {
        this.attribute = attribute;
        return this;
    }

    public boolean doesExist() {
        return exists;
    }

    public AttributeIdentifier<T> setExists(boolean exists) {
        this.exists = exists;
        return this;
    }

    @Override
    public String toString() {
        if(!exists) {
            return "No Attribute found";
        }
        return getAttribute().toString();
    }

}
