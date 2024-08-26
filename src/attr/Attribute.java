package type.attr;

public class Attribute<T> {
    private final String lookup;
    private T data;

    public Attribute(String lookup, T data) {
        this.lookup = lookup;
        this.data = data;
    }

    public String lookup() {
        return lookup;
    }

    public T data() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private String cleanString(String in) {
        while (Character.isWhitespace(in.charAt(0))) {
            in = in.substring(1);
        }
        while (Character.isWhitespace(in.charAt(in.length() - 1))) {
            in = in.substring(0, in.length() - 1);
        }
        return in;
    }

    @Override
    public String toString() {
        return String.format("\t%s: %s", lookup, data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute<?> attribute = (Attribute<?>) o;

        if (!lookup.equals(attribute.lookup)) return false;
        return data.equals(attribute.data);
    }

    @Override
    public int hashCode() {
        int result = lookup.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }
}
