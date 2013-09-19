package pl.stock.algorithm.core;

/**
 * Class for calculating Relative Strength Index (RSI)
 * @author Piotr Mińkowski
 *
 */
public class RSI {

	// period in days for count RSI
	private int period;

	// EMA for 2*period-1 period in RSI formula 
	private EMA ema;

	/**
	 * Constructor
	 * @param period - set period
	 */
	public RSI(final int period) {
		this.period = period;
		this.ema = new EMA(2 * period - 1);
	}

	/**
	 * Count RSI
	 * @param prizes - prizes table for specified period
	 * @return - RSI table
	 */
	public double[] count(final double[] prizes) {

		// fill 'up' and 'down' table - 'up' when today prize is bigger than yesterday, 'down' when today is lower than yesterday
		final double[] up = new double[prizes.length - 1];
		final double[] down = new double[prizes.length - 1];
		for (int i = 0; i < prizes.length - 1; i++) {
			if (prizes[i] > prizes[i + 1]) {
				up[i] = prizes[i] - prizes[i + 1];
				down[i] = 0;
			}
			if (prizes[i] < prizes[i + 1]) {
				down[i] = Math.abs(prizes[i] - prizes[i + 1]);
				up[i] = 0;
			}
		}

		// count EMA for up and down tables
		final int emaLength = prizes.length - 2 * period;
		double[] rsis = new double[0];
		if (emaLength > 0) {
			final double[] emus = new double[emaLength];
			final double[] emds = new double[emaLength];
			ema.count(up, 0, emus);
			ema.count(down, 0, emds);

			// count RSI with RSI recursive formula
			rsis = new double[emaLength];
			for (int i = 0; i < rsis.length; i++) {
				rsis[i] = 100 - (100 / (double) (1 + emus[i] / emds[i]));
			}
		}

		return rsis;
	}
}
