package pt.utl.ist.fenix.tools.file;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

public class FileSetQueryResults implements XMLSerializable {

	private Set<FileSetDescriptor> results = new HashSet<FileSetDescriptor>();
	private int start = 0;
	private int pageSize = 10;
	private int hitsCount = 0;

	/**
	 * @return the hitsCount
	 */
	public int getHitsCount() {
		return hitsCount;
	}

	/**
	 * @param hitsCount the hitsCount to set
	 */
	public void setHitsCount(int hitsCount) {
		this.hitsCount = hitsCount;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the results
	 */
	public Set<FileSetDescriptor> getResults() {
		return results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(List<FileSetDescriptor> results) {
		for (FileSetDescriptor descriptor : results) {
			this.results.add(descriptor);
		}
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public void fromXMLString(String xml) {
		try {
			fromXML(org.dom4j.DocumentHelper.parseText(xml).getRootElement());
		} catch (DocumentException e) {
			throw new RuntimeException("Error parsing xml string : " + xml, e);
		}
	}

	@Override
	public String toXMLString() {
		return toXML().asXML();
	}

	@SuppressWarnings("unchecked")
	public void fromXML(Element el) {

		setStart(Integer.parseInt(el.attributeValue("start")));
		setPageSize(Integer.parseInt(el.attributeValue("pagesize")));
		setHitsCount(Integer.parseInt(el.attributeValue("hitscount")));

		for (Element result : (List<Element>) el.elements()) {
			FileSetDescriptor descriptor = new FileSetDescriptor();
			descriptor.fromXML(result);
			results.add(descriptor);
		}
	}

	public Element toXML() {
		Element el = new BaseElement("filesetqueryresults");

		el.addAttribute("start", "" + getStart());
		el.addAttribute("pagesize", "" + getPageSize());
		el.addAttribute("hitscount", "" + getHitsCount());
		for (FileSetDescriptor descriptor : getResults()) {
			el.add(descriptor.toXML());
		}

		return el;
	}

}
