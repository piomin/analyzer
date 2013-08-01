package pl.stock.commons.parser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import pl.stock.data.entity.DailyQuoteRecord;

/**
 * Class for parsing input daily quote data file
 * @author Piotr Mińkowski
 *
 */
@Component
public class DailyQuotesFileParser extends QuotesFileParser {
	
	
	@Override
	public List<DailyQuoteRecord> parse(File file) throws ParseException {
		
		// file stream handle
		BufferedInputStream stream = null;
		
		// return list with daily quote records
		List<DailyQuoteRecord> records = null;
		
		try {
			// opening file stream
			stream = new BufferedInputStream(new FileInputStream(file));
			
			// reading file lines and translating into DailyQuoteRecord object in loop
			final List<String> lines = IOUtils.readLines(stream);
			records = translateLines(lines);
		} catch (FileNotFoundException e) {
			LOGGER.error("", e);
		} catch (IOException e) {
			LOGGER.error("", e);
		} finally {
			IOUtils.closeQuietly(stream);
		}
		
		return records;
	}
	
}
