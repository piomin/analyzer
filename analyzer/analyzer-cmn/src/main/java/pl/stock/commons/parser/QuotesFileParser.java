package pl.stock.commons.parser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.format.datetime.DateFormatter;

import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;

/**
 * Abstract class for all file data parsers
 * @author Piotr Mińkowski
 *
 */
public abstract class QuotesFileParser {

	// string splitting line in quote file record
	private static final String QUOTE_SPLIT_CHAR = ",";
	
	// log object
	protected final Logger LOGGER = Logger.getLogger(this.getClass());
	
	// date 'yyyyMMdd' formatter
	private static final DateFormatter FORMATTER = new DateFormatter("yyyyMMdd");
	
	
	/**
	 * Translating all string lines form file into used objects
	 * @param lines - list with lines from file
	 * @return - list with DailyQuoteRecord objects
	 * @throws ParseException - error while parsing file
	 */
	protected List<DailyQuoteRecord> translateLines(final List<String> lines) throws ParseException {
		
		// return list with daily quote records
		final List<DailyQuoteRecord> records = new ArrayList<DailyQuoteRecord>();
		
		// iterating all lines form list
		for (String line : lines) {				
			// parsing line, creating record object
			final DailyQuoteRecord record = parseLine(line);
			
			// add created daily record to the returned list
			records.add(record);
		}
		
		return records;
	}
	
	
	/**
	 * Parsing line in quote file
	 * @param line - input line
	 * @return - daily quote record
	 * @throws ParseException - error while parsing file
	 */
	private DailyQuoteRecord parseLine(String line) throws ParseException {
		
		// splitting line into string table
		final String[] temp = line.split(QUOTE_SPLIT_CHAR);
		
		// index for counting
		int index = 0;
		
		// creating and filling record object
		final DailyQuoteRecord record = new DailyQuoteRecord();
		final Company company = new Company();
		company.setSymbol(temp[index++]);
		record.setCompany(company);
		record.setDate(FORMATTER.parse(temp[index++], Locale.getDefault()));
		record.setOpen(Double.parseDouble(temp[index++]));
		record.setMax(Double.parseDouble(temp[index++]));
		record.setMin(Double.parseDouble(temp[index++]));			
		record.setClose(Double.parseDouble(temp[index++]));
		record.setVolumen(Double.parseDouble(temp[index++]));
		
		return record;
	}
	
	
	/**
	 * Parsing method
	 * @param file - input file
	 * @return - list of output records
	 * @throws ParseException
	 */
	public abstract List<DailyQuoteRecord> parse(File file) throws ParseException, IOException;
	
}
