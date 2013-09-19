package pl.stock.commons.parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.stock.data.entity.DailyQuoteRecord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class FileDataTest {

	// log object
	private final Logger LOGGER = Logger.getLogger(FileDataTest.class);
	
	// daily quote file parser object
	private DailyQuotesFileParser parser = new DailyQuotesFileParser();
	
	// multiple days quote file parser object
	private MultiQuotesFileParser multiParser = new MultiQuotesFileParser();
	
	
	/**
	 * Test if downloading file with daily quotes works
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Test
	public void downloadDailyFileTest() throws ParseException, IOException {
		URL url = new URL("http://bossa.pl/pub/metastock/mstock/sesjaall/sesjaall.prn");
		File temp = File.createTempFile("_daily", ".temp");
		FileUtils.copyURLToFile(url, temp);
		List<DailyQuoteRecord> records = parser.parse(temp);
		LOGGER.info("Records read -> " + records.size());
	}
	
	
	/**
	 * Test if downloading file with multiple quotes works
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Test
	public void downloadMultiFileTest() throws ParseException, IOException {
		URL url = new URL("http://bossa.pl/pub/metastock/mstock/mstall.zip");
		File temp = File.createTempFile("_daily", ".temp");
		FileUtils.copyURLToFile(url, temp);
		Map<String, List<DailyQuoteRecord>> records = multiParser.parse(temp);
		LOGGER.info("Records read -> " + records.size());
	}
	
}
