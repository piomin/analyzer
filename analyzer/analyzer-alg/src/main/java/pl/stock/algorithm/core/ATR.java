package pl.stock.algorithm.core;

import org.apache.commons.math.stat.StatUtils;

/**
 * Class for calculating Average True Range (ATR)
 * @author Piotr Mińkowski
 *
 */
public class ATR {

	// period in days for count ATR
	private int period;
	
	// EMA for 2*period in ATR formula
	private EMA ema;
	
	/**
	 * Constructor
	 * @param period - set period
	 */
	public ATR(final int period) {
		this.period = period;
		this.ema = new EMA(period);
	}
	
	
	/**
	 * Calculating Average True Range (ATR)
	 * @param closes - close prizes for specified period
	 * @param mins - minimum prizes per day for specified period 
	 * @param maxs - maximum prizes per day for specified period 
	 * @return - ATR table
	 */
	public double[] count(double[] closes, double[] mins, double[] maxs) {
		
		// count ATR per day as maximum of ranges: maximum - minimum, close previous day - maximum, close previous day - minimum
		final double[] trs = new double[closes.length - 1];
		final double[] trDay = new double[3];
		for (int i = 0; i < closes.length - 1; i++) {
			trDay[0] = maxs[i] - mins[i];
			trDay[1] = Math.abs(closes[i+1] - maxs[i]);
			trDay[2] = Math.abs(closes[i+1] - mins[i]);
			trs[i] = StatUtils.max(trDay);
		}
		
		// count EMA for ATR table
		final double[] emaTrsTab = new double[closes.length - 1 - period];
		ema.count(trs, 0, emaTrsTab);
		
		return emaTrsTab;
	}
	
	
	/**
	 * Calculating Average True Range (ATR) for single day
	 * @param closeYesterday - close prize for previous day
	 * @param min - minimum prize today
	 * @param max - maximum prize today
	 * @param previousAtr - previous ATR value
	 * @return - today ATR
	 */
	public double single(double closeYesterday, double min, double max, double previousAtr) {
		
		// count ATR per day as maximum of ranges: maximum - minimum, close previous day - maximum, close previous day - minimum
		final double tr1 = max - min;
		final double tr2 = Math.abs(closeYesterday - max);
		final double tr3 = Math.abs(closeYesterday - min);
		final double trs = StatUtils.max(new double[] {tr1, tr2, tr3});
		
		// count EMA for ATR
		final double atr = ema.single(trs, previousAtr);
		
		return atr;
	}
}
