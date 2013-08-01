package pl.stock.data.core;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.beans.UpdateStatus;
import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.entity.StatisticRecord;
import pl.stock.data.entity.UpdateHistory;
import pl.stock.data.service.CompanyService;
import pl.stock.data.service.DailyQuoteRecordService;
import pl.stock.data.service.StatisticRecordService;
import pl.stock.data.service.UpdateHistoryService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(readOnly=false)
@TransactionConfiguration(defaultRollback=false)
public class StockDataTest {

	// log4j object
	private final Logger LOGGER = Logger.getLogger(StockDataTest.class);

	// company service object
	@Autowired
	private CompanyService companyService;
	
	// update history service object
	@Autowired
	private UpdateHistoryService updateService;
	
	// daily quote record service object
	@Autowired
	private DailyQuoteRecordService quoteService;
	
	// statistic record service object
	@Autowired
	private StatisticRecordService statisticService;
	
	/**
	 * Test table modifications
	 */
	//@Test
	public void testModifications() {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		final Date date = new Date();
		
		// test company service
		Company company = new Company();
		company.setName("Testowa" + sdf.format(date));
		company.setSymbol("TEST" + sdf.format(date));
		final Integer companyId = companyService.add(company);
		LOGGER.info("Company added, id -> " + companyId);
		company.setName("Firma krzak" + sdf.format(date));
		company = (Company) companyService.save(company);
		LOGGER.info("Company name changed to " + company.getName());
		
		// test update history service
		UpdateHistory update = new UpdateHistory();
		update.setAddDate(date);
		update.setStatus(UpdateStatus.ERROR);
		final Integer updateId = updateService.add(update);
		LOGGER.info("Update history added, id -> " + updateId);
		update.setStatus(UpdateStatus.SUCCESS);
		update = (UpdateHistory) updateService.save(update);
		LOGGER.info("Update history status changed to " + update.getStatus());
		
		// test daily quote record service
		DailyQuoteRecord record = new DailyQuoteRecord();
		record.setCompany(company);
		record.setDate(date);
		record.setOpen(10.23);
		record.setClose(10.45);
		record.setMin(10.01);
		record.setMax(10.45);
		record.setVolumen(12342);
		final Long recordId = quoteService.add(record);
		LOGGER.info("Quote record added, id -> " + recordId);
		
		// test statistic for quote, set all values on 0
		StatisticRecord statistic = new StatisticRecord();
		statistic.setQuote(record);
		statistic.setAddDate(date);
		final Long statisticId = statisticService.add(statistic);
		LOGGER.info("Statistic record added, id -> " + statisticId);
		
		// test finished successfully
		LOGGER.info("Modifications succesfully finished");
	}
	
	
	/**
	 * Test table reads
	 */
	@Test
	public void testRead() {
		
		// test company service
		final Company company = (Company) companyService.load(1);
		LOGGER.info("Company found -> " + company.getShortInfo());
		
		// test update history service
		final UpdateHistory updateHistory = (UpdateHistory) updateService.load(1);
		LOGGER.info("UpdateHistory found -> " + updateHistory.getShortInfo());
		
		// test daily quote record service
		DailyQuoteRecord quote = (DailyQuoteRecord) quoteService.load(1L);
		LOGGER.info("DailyQuoteRecord found -> " + quote.getShortInfo());
		
		// test statistic service
		final StatisticRecord statistic = (StatisticRecord) statisticService.load(1L);
		LOGGER.info("StatisticRecord found -> " + statistic.getShortInfo());
		
		// test update history service count() method
		final int count = updateService.count();
		LOGGER.info("UpdateHistory count -> " + count);
		
		// test daily quote record service findByCompany() method 
		quote = quoteService.findLastByCompany(company);
		LOGGER.info("DailyQuoteRecord found -> " + quote.getShortInfo());
		
		// test finished successfully
		LOGGER.info("Reads succesfully finished");
	}
	
}
