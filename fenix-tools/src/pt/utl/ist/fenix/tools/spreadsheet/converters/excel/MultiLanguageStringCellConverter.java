package pt.utl.ist.fenix.tools.spreadsheet.converters.excel;

import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class MultiLanguageStringCellConverter implements CellConverter {

	private Language language = null;

	public MultiLanguageStringCellConverter() {
	}

	public MultiLanguageStringCellConverter(final Language language) {
		this.language = language;
	}

	@Override
	public Object convert(Object source) {

		if (source != null) {
			final MultiLanguageString value = (MultiLanguageString) source;
			return (language != null) ? value.getContent(language) : value.getContent();
		}

		return null;
	}

}
