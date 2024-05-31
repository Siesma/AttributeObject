package type.attr;

import jdk.jfr.Label;

public record Attribute<T>(String lookup, T data) {
    @Label("Unused")
    private String cleanString (String in) {
        while(Character.isWhitespace(in.charAt(0))) {
            in = in.substring(1);
        }
        while(Character.isWhitespace(in.charAt(in.length() - 1))) {
            in = in.substring(0, in.length() - 1);
        }
        return in;
    }
    @Override
    public String toString () {
//        return String.format("\"%s\":\n\t%s", this.lookup, cleanString(data.toString()).replaceAll("\n", "\n\t"));
        return String.format("\t%s: %s\n", lookup(), data());
    }
}
