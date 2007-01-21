package pt.linkare.ant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSSTextBlockParser implements TextBlockParser {

    public List<TextBlock> readBlocks(Reader in) throws IOException {
	List<TextBlock> outList = new ArrayList<TextBlock>();

	BufferedReader br = (in instanceof BufferedReader) ? (BufferedReader) in
		: new BufferedReader(in);

	String line = null;
	String previousContentFinish = null;
	String startNextContent = null;

	boolean multilineComment = false;
	boolean commentBlock = false;
	boolean cssBlock = false;
	
	StringBuffer content = new StringBuffer();
	while ((line = br.readLine()) != null) {

	    if (line.trim().length() == 0)
		continue;

	    if(line.contains("{"))
		cssBlock=true;
	    
	    if (line.trim().startsWith("//")
		    || (line.trim().startsWith("/*") && line.trim().endsWith("*/"))) {
		previousContentFinish = line;
		commentBlock = true;
	    } else if (line.contains("/*") && !cssBlock) {
		previousContentFinish = line.substring(0, line.indexOf("/*"));
		startNextContent = line.substring(line.indexOf("/*"));
		multilineComment = true;
	    } else if (line.contains("*/") && !cssBlock) {
		previousContentFinish = line.substring(0, line.indexOf("*/") + 2);
		startNextContent = line.substring(line.indexOf("*/") + 2);
		commentBlock = true;
		multilineComment = false;
	    } else if (!multilineComment && line.contains("}")) {
		previousContentFinish = line.substring(0, line.indexOf("}") + 1);
		startNextContent = line.substring(line.indexOf("}") + 1);
		cssBlock=false;
	    } else {
		previousContentFinish = line;
		startNextContent = null;
	    }

	    content.append(previousContentFinish).append(CRLF);
	    if (startNextContent != null) {

		if (content.toString().trim().length() != 0) {
		    TextBlock block = null;
		    if (commentBlock) {
			block = new UnknownTextBlock();
			commentBlock = false;
		    } else
			block = new CSSTextBlock();

		    block.setContent(content.toString());
		    outList.add(block);
		}

		content = new StringBuffer(startNextContent);
		content.append(CRLF);
	    }
	}

	return outList;
    }

}
