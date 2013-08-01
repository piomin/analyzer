package pl.stock.algorithm.core;

import org.apache.commons.math.stat.StatUtils;

public class EMA {

	// period in days for count EMA
	private int period;
	
	/**
	 * Constructor
	 * @param period - set period
	 */
	public EMA(final int period) {
		this.period = period;
	}
	
	
	/**
	 * Count EMA for period
	 * @param prizes - input table with prizes sort from latest day to earliest
	 * @param offset - offset in the prizes table
	 * @param emas - empty table where EMA-s count would be inserted
	 * @return - EMA
	 */
	public double count(final double[] prizes, final int offset, final double[] emas) {
		
		// check if period is not longest than number of prizes count from period index
		if (prizes.length - offset <= period) {
			final double[] startPrizes = new double[period];
			System.arraycopy(prizes, offset, startPrizes, 0, period);
			// returning simple mean for that period, because it's beginning of count EMA
			return StatUtils.mean(startPrizes, 0, period);
			
		// if period is longer we use recursion algorithm	
		} else {
			// recursion call with offset incremented
			final double previousEma = count(prizes, offset + 1, emas);
			// count from the EMA recursion formula
			final double a = (double) 2 / (period + 1);
			final double actualEma = (prizes[offset] - previousEma) * a + previousEma;
			emas[offset] = actualEma;
			return actualEma;
		}
	}
	
	
	/**
	 * Count EMA for one day
	 * @param prize - actual prize
	 * @param previousEma - previous EMA value
	 * @return - EMA for today
	 */
	public double single(final double prize, final double previousEma) {
		final double a = (float) 2 / (period + 1);
		final double actualEma = (prize - previousEma) * a + previousEma;
		return actualEma;
	}
	
}
