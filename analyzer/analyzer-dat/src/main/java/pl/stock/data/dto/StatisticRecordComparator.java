package pl.stock.data.dto;

import java.util.Comparator;

import pl.stock.data.entity.StatisticRecord;

public class StatisticRecordComparator implements Comparator<StatisticRecord> {

    @Override
    public int compare(StatisticRecord o1, StatisticRecord o2) {
	return o1.getAddDate().compareTo(o2.getAddDate());
    }

}
