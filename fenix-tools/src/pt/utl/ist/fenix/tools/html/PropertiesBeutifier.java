package pt.utl.ist.fenix.tools.html;

public class PropertiesBeutifier extends PropertiesConverter {

    @Override
    public String convert(final String string) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            final char c = string.charAt(i);
            if (i == 0) {
                stringBuilder.append(Character.toUpperCase(c));
            } else {
                if (Character.isUpperCase(c)) {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString().replace(" I D", " Id");
    }

}
