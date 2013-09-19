package pl.stock.logic.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class IndicesListHelper {

	public static Document getDocument(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

	public static Element getElement(Document doc, String id) {
		return doc.getElementById(id);
	}

	public static Element getElementByType(Element elem, String type) {
		return elem.getElementsByTag(type).first();
	}

	public static Elements getAllElementsByType(Element elem, String type) {
		return elem.getElementsByTag(type);
	}

	public static List<String> findAllNames(Document doc, String id, String type, String listType, String valueType) {
		List<String> names = new ArrayList<>();
		Element searchType = getElementByType(getElement(doc, id), type);
		Elements elements = getAllElementsByType(searchType, listType);
		for (Element element : elements) {
			Element value = getElementByType(element, valueType);
			if (value != null) {
				names.add(value.text());
			}
		}
		return names;
	}
	
}
