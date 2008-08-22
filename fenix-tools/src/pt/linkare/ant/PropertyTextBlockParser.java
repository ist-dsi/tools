package pt.linkare.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class PropertyTextBlockParser implements TextBlockParser {

	public List<TextBlock> readBlocks(Reader in) throws IOException {
		List<TextBlock> outList = new ArrayList<TextBlock>();

		BufferedReader br = (in instanceof BufferedReader) ? (BufferedReader) in
				: new BufferedReader(in);

		String line = null;
		String previousContentFinish = null;
		String startNextContent = null;

		boolean continuation=false;
		StringBuilder content = new StringBuilder();
		while ((line = br.readLine()) != null) {

			if (line.trim().length() == 0)
				continue;

			if (line.trim().startsWith("#")) {
				previousContentFinish = "";
				startNextContent = line+CRLF;
			} else if (line.contains("=") && !continuation) {
				previousContentFinish = "";
				startNextContent = line+CRLF;
			} else {
				previousContentFinish = line+CRLF;
				startNextContent = null;
			}
			//while we have a continuation line just keep ignoring a new = sign
			continuation=line.endsWith("\\");
			

			content.append(previousContentFinish);
			if (startNextContent != null) {
				if (content.toString().trim().length() != 0) {
					TextBlock block = null;
					if (content.toString().trim().startsWith("#"))
						block = new UnknownTextBlock();
					else
						block = new PropertyTextBlock();

					block.setContent(content.toString());
					outList.add(block);
				}

				content = new StringBuilder(startNextContent);
			}

		}
		if (content.toString().trim().length() != 0) {
			TextBlock block = null;
			if (content.toString().trim().startsWith("#"))
				block = new UnknownTextBlock();
			else
				block = new PropertyTextBlock();

			block.setContent(content.toString());
			outList.add(block);
		}

		return outList;
	}

}
