package pt.utl.ist.fenix.tools.spreadsheet.converters.excel;

import java.util.Locale;

import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MultiLanguageStringCellConverter implements CellConverter {

    private Locale locale = null;

    public MultiLanguageStringCellConverter() {
    }

    public MultiLanguageStringCellConverter(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public Object convert(Object source) {

        if (source != null) {
            final MultiLanguageString value = (MultiLanguageString) source;
            return (locale != null) ? value.getContent(locale) : value.getContent();
        }

        return null;
    }

}
