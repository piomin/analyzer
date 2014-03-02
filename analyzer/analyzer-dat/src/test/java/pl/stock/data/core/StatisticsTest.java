package pl.stock.data.core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.service.DailyQuoteRecordService;
import pl.stock.data.service.StatisticRecordService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional(readOnly=false)
public class StatisticsTest {

	private static final Logger LOGGER = Logger.getLogger(StatisticsTest.class);
	
	@Autowired
	private StatisticRecordService statisticService;
	@Autowired
	private DailyQuoteRecordService quoteService;
	
	@Test
	public void testStatisticRead() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2013, Calendar.SEPTEMBER, 9);
		Date to = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -20);
		Date from = calendar.getTime();
		LOGGER.info("testStatisticRead start");
		statisticService.findByDateAndIds(from, to, new Integer[] {1,2,3,4,5});
		LOGGER.info("testStatisticRead end");
	}
	
	@Test
	public void testQuotesRead() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2013, Calendar.SEPTEMBER, 9);
		Date to = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -20);
		Date from = calendar.getTime();
		LOGGER.info("testQuotesRead start");
		quoteService.findByDateAndIds(from, to, new Integer[] {1,2,3,4,5});
		LOGGER.info("testQuotesRead end");
	}
	
	@Test
	public void testQuotes2Read() {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(2013, Calendar.SEPTEMBER, 9);
		Date to = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, -20);
		Date from = calendar.getTime();
		LOGGER.info("testQuotes2Read start");
		quoteService.findByDatePeriod(from, to);
		LOGGER.info("testQuotes2Read end");
	}
}
