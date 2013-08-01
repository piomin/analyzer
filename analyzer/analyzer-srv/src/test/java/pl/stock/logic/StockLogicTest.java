package pl.stock.logic;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StockLogicTest {

	// log4j object
	private final Logger LOGGER = Logger.getLogger(StockLogicTest.class);
	
	@Test
	public void testSchedule() {
		LOGGER.debug("Waiting for schedule.");
		try {
			Thread.sleep(800000);
		} catch (InterruptedException e) {
			LOGGER.error("EXCEPTION.SCHEDULE.THREAD", e);
		}
		LOGGER.debug("Finished waiting for schedule");
	}
	
}
