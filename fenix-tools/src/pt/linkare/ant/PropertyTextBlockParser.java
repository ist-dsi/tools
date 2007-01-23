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

	StringBuffer content = new StringBuffer();
	while ((line = br.readLine()) != null) {

	    if (line.trim().length() == 0)
		continue;

	    line=line+CRLF;
	    if (line.trim().startsWith("#")) {
		previousContentFinish = "";
		startNextContent = line;
	    } else if (line.contains("=")) {
		previousContentFinish = "";
		startNextContent = line;
	    } else {
		previousContentFinish = line;
		startNextContent = null;
	    }

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

		content = new StringBuffer(startNextContent);
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
