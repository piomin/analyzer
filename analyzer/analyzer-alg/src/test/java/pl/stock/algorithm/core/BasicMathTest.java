package pl.stock.algorithm.core;

import java.util.Arrays;

import org.apache.commons.math.stat.StatUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class BasicMathTest {

	private final Logger LOGGER = Logger.getLogger(BasicMathTest.class);
	
	
	/**
	 * Basic commons-math statistic tests
	 */
	@Test
	public void statisticTest() {
		
		// shared result variable
		double sResult;
		double[] vResult;
		
		// input test data
		final double[] sData = {1, 2, 3, 4};
		final double[] vData = {0, 0.5, 1.25, 1.75, 2.2, 3};
		
		// normal mean
		sResult = StatUtils.mean(sData);
		LOGGER.info("SIM.MEAN -> " + sResult);
		sResult = StatUtils.mean(sData, 0, 3);
		LOGGER.info("SIM.MEAN -> " + sResult);
		
		// geometric mean
		sResult = StatUtils.geometricMean(sData);
		LOGGER.info("GEO.MEAN -> " + sResult);
		
		// normalize array
		vResult = StatUtils.normalize(vData);
		LOGGER.info("ARR.NORM -> " + Arrays.toString(vResult));
	}
	
	
}
