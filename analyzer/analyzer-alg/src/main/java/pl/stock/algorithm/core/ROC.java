package pl.stock.algorithm.core;

/**
 * Class for calculating Rate of Change (RoC)
 * @author Piotr Mińkowski
 *
 */
public class ROC {

	// period for count RoC
	private int period;

	// period for count EMA in RoC table
	private int averagePeriod;

	// EMA for RoC
	private EMA ema;

	/**
	 * Constructor
	 * @param period - set period
	 * @param averagePeriod - set average period
	 */
	public ROC(final int period, final int averagePeriod) {
		this.period = period;
		this.averagePeriod = averagePeriod;
		this.ema = new EMA(averagePeriod);
	}

	/**
	 * Calculating Rate of Change (RoC) for all days in input table
	 */
	public double[][] count(final double[] prizes) {

		// count RoC table - as difference between today prize and in period day
		final double[] rocs = new double[prizes.length - period];
		for (int i = 0; i < rocs.length; i++) {
			rocs[i] = 100 * (prizes[i] - prizes[i + period]) / prizes[i + period];
		}

		// count EMA for RoC table 
		double[] srocs = new double[0];
		if (rocs.length > averagePeriod) {
			srocs = new double[rocs.length - averagePeriod];
			ema.count(rocs, 0, srocs);
		}

		// fill return table - [0] RoC table, [1] EMA for RoC table
		final double[][] retTable = new double[2][];
		retTable[0] = rocs;
		retTable[1] = srocs;

		return retTable;
	}

	/**
	 * Calculating Rate of Change (RoC) for single day
	 * @param prizes - prizes table
	 * @return - RoC value
	 */
	public double single(final double[] prizes) {
		final double roc = 100 * (prizes[0] - prizes[period]) / prizes[period];
		return roc;
	}

}
