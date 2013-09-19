package pl.stock.data.service;

import java.util.List;

import pl.piomin.core.data.generic.GenericService;
import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;

public interface DailyQuoteRecordService extends GenericService<Long, DailyQuoteRecord> {

	DailyQuoteRecord findLastByCompany(Company company);
	
	int countByCompany(Company company);
	
	List<DailyQuoteRecord> findByCompany(Company company, int maxCount);
	
	List<DailyQuoteRecord> findAllByCompany(Company company);
	
}
