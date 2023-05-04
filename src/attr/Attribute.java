package type.attr;

public class Attribute<T> {
    private String lookup;
    private T data;

    public Attribute(String lookup, T data) {
        this.lookup = lookup;
        this.data = data;
    }

    public String getLookup() {
        return lookup;
    }

    public Attribute<T> setLookup(String lookup) {
        this.lookup = lookup;
        return this;
    }

    public T getData() {
        return data;
    }

    public Attribute<T> setData(T data) {
        this.data = data;
        return this;
    }

    public String reducedData() {
        return "";
    }

    @Override
    public String toString() {
        String cleanedData = this.data.toString();
        while (cleanedData.endsWith(" ") || cleanedData.endsWith("\t") || cleanedData.endsWith("\n"))
            cleanedData = cleanedData.substring(0, cleanedData.length() - 1);

        while (cleanedData.startsWith(" ") || cleanedData.startsWith("\t") || cleanedData.startsWith("\n"))
            cleanedData = cleanedData.substring(1);


        return this.lookup + "; " + "\"" + cleanedData + "\"";
    }


}
