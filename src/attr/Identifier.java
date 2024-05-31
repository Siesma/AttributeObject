package type.attr;

public class Identifier {
    private String lookup;
    private Identifier[] equalIdentifiers;

    public Identifier (String lookup, Identifier... equalIdentifiers) {
        this.lookup = lookup;
        this.equalIdentifiers = equalIdentifiers;
    }

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public Identifier[] getEqualIdentifiers() {
        return equalIdentifiers;
    }

    public void setEqualIdentifiers(Identifier[] equalIdentifiers) {
        this.equalIdentifiers = equalIdentifiers;
    }

    public void addEqualIdentifier (Identifier other) {
        Identifier[] temp = new Identifier[equalIdentifiers.length  + 1];
        System.arraycopy(equalIdentifiers, 0, temp, 0, equalIdentifiers.length);
        temp[temp.length - 1] = other;
        this.equalIdentifiers = temp;
    }

}
