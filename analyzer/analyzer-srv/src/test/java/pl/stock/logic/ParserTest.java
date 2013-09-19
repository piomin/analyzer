package pl.stock.logic;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.junit.Test;

import pl.stock.logic.helper.IndicesListHelper;

public class ParserTest {

	@Test
	public void test() throws IOException {
		Document doc = IndicesListHelper.getDocument("http://www.stockwatch.pl/gpw/indeks/mwig40,sklad.aspx");
		IndicesListHelper.findAllNames(doc, "StockIndex", "tbody", "td", "a");
	}
	
}
