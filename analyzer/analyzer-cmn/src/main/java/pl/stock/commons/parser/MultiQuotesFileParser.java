package pl.stock.commons.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import pl.stock.data.entity.DailyQuoteRecord;

/**
 * Class for parsing quotes form file for some days
 * @author Piotr Mińkowski
 *
 */
@Component
public class MultiQuotesFileParser extends QuotesFileParser {

	@Override
	public Map<String, List<DailyQuoteRecord>> parse(File file, Pattern blacklist) throws ParseException, IOException {

		// creating ZIP handle for input file
		final ZipFile zip = new ZipFile(file);

		// return list with daily quote records
		final Map<String, List<DailyQuoteRecord>> records = new HashMap<String, List<DailyQuoteRecord>>();

		// read each entry in input stream and parse that file
		final Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			InputStream stream = null;
			try {
				// read bytes for one ZIP entry
				final ZipEntry entry = entries.nextElement();
				stream = zip.getInputStream(entry);

				final String company = entry.getName().substring(0, entry.getName().length() - 4);
				if (!blacklist.matcher(company).matches()) {
					// reading file lines, removing title line and translating into DailyQuoteRecord object in loop
					final List<String> lines = IOUtils.readLines(stream);
					final String titleLine = lines.remove(0);
					LOGGER.debug(MessageFormat.format("Title line {0} removed.", titleLine));
					records.put(company, translateLines(lines, blacklist));

					// finished entry log
					LOGGER.info(MessageFormat.format("Zip entry {0} translated.", entry.getName()));
				}
			} catch (FileNotFoundException e) {
				LOGGER.error("", e);
			} catch (IOException e) {
				LOGGER.error("", e);
			} finally {
				IOUtils.closeQuietly(stream);
				zip.close();
			}
		}

		return records;
	}

}
