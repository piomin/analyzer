package pl.stock.algorithm.core;

import org.apache.commons.math.stat.StatUtils;

/**
 * Class for calculating STS Stochastic
 * @author Piotr Mińkowski
 *
 */
public class STS {

	// period for count STS
	private int period;
	
	// period for count EMA for STS table
	private int averagePeriod;
	
	// period for count SMA
	private int sPeriod;
	
	// EMA for STS
	private EMA ema;
	
	/**
	 * Constructor
	 * @param period - set period
	 * @param averagePeriod - set average period
	 * @param sPeriod - set sma period
	 */
	public STS(final int period, final int averagePeriod, final int sPeriod) {
		this.period = period;
		this.averagePeriod = averagePeriod;
		this.sPeriod = sPeriod;
		this.ema = new EMA(averagePeriod);
	}
	
	
	/**
	 * Calculating STS Stochastic
	 * @param closes - close prizes for specified period
	 * @param mins - minimum prizes per day for specified period
	 * @param maxs - maximum prizes per day for specified period
	 * @return - STS table
	 */
	public double[][] count(double[] closes, double[] mins, double[] maxs) {
		
		// count simple STS for each day during period
		final int ksLength = closes.length - period + 1;
		final double[] ks = new double[ksLength]; 
		for (int i = 0; i < ksLength; i++) {
			final double min = StatUtils.min(mins, i, period);
			final double max = StatUtils.max(maxs, i, period);
			ks[i] = 100 * (closes[i] - min) / (max - min);
		}
		
		// count SMA for simple STS table
		double[] sts = new double[ksLength - sPeriod + 1];
		double[] actualKs = new double[sPeriod];
		for (int i = 0; i < sts.length; i++) {
			System.arraycopy(ks, i, actualKs, 0, sPeriod);
			sts[i] = StatUtils.mean(actualKs, 0, sPeriod);
		}
		
		// count STS slow
		double[] stsSlow = new double[sts.length - averagePeriod + 1];
		ema.count(sts, 0, stsSlow);
		
		// fill return table - [0] SMA for STS, [1] STS slow
		double[][] retTable = new double[2][];
		retTable[0] = sts;
		retTable[1] = stsSlow;
		
		return retTable;
	}
	
	
	/**
	 * Calculating STS Stochastic for single day
	 * @param closes - closes prize table for requested period
	 * @param mins - minimum prize table for requested period
	 * @param maxs - maximum prize table for requested period
	 * @param previousStsSlow - EMA STS value for previous day
	 * @return - [0] - STS, [1] - EMA STS
	 */
	public double[] single(double[] closes, double[] mins, double[] maxs, double previousStsSlow) {
		
		// count STS table for requested period
		final int ksLength = closes.length - period + 1;
		final double[] ks = new double[ksLength]; 
		for (int i = 0; i < ksLength; i++) {
			final double min = StatUtils.min(mins, i, period);
			final double max = StatUtils.max(maxs, i, period);
			ks[i] = 100 * (closes[i] - min) / (max - min);
		}
		
		// count STS value and EMA STS
		final double sts = StatUtils.mean(ks, 0, sPeriod);
		final double stsSlow = ema.single(sts, previousStsSlow);
		
		// fill return table: [0] - STS, [1] - EMA STS
		return new double[] { sts, stsSlow };
	}
}
