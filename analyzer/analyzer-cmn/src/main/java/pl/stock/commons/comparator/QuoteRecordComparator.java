package pl.stock.commons.comparator;

import java.util.Comparator;
import java.util.Date;

import pl.stock.data.entity.DailyQuoteRecord;

/**
 * Comparator for sorting daily quotes list 
 * @author Piotr Mińkowski
 *
 */
public class QuoteRecordComparator implements Comparator<DailyQuoteRecord> {

	@Override
	public int compare(DailyQuoteRecord record1, DailyQuoteRecord record2) {
		final Date date1 = record1.getDate();
		final Date date2 = record2.getDate();
		return date1.compareTo(date2);
	}

}
