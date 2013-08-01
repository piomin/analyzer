package pl.stock.algorithm.core;


public class MACD {

	// period in days for count fast MACD
	private int periodShort;
	
	// period in days for count slow MACD
	private int periodLong;
	
	// period in days for main MACD
	private int periodSignal;
	
	// EMA for fast MACD
	private EMA emaShort;
	
	// EMA for slow MACD
	private EMA emaLong;
	
	// EMA for main MACD
	private EMA emaSignal;
	
	/**
	 * Constructor
	 * @param period - set period
	 */
	public MACD(final int periodSignal, final int periodShort, final int periodLong) {
		this.periodSignal = periodSignal;
		this.periodShort = periodShort;
		this.periodLong = periodLong;
		emaSignal = new EMA(periodSignal);
		emaShort = new EMA(periodShort);
		emaLong = new EMA(periodLong);
	}
	
	
	/**
	 * Count MACD for period
	 * @param prizes - table with prizes for count period
	 * @return
	 */
	public double[][] count(final double[] prizes) {
		
		// count EMA table for short period
		final double[] emasShort = new double[prizes.length - periodShort];
		emaShort.count(prizes, 0, emasShort);
		
		// count EMA table for long period
		final double[] emasLong = new double[prizes.length - periodLong];
		emaLong.count(prizes, 0, emasLong);
		
		// count MACD - difference between EMA table
		final double[] actualMacds = new double[prizes.length - periodLong];
		for (int i = 0; i < actualMacds.length; i++) {
			actualMacds[i] = emasShort[i] - emasLong[i];
		}
		
		// count main MACD-s EMA table
		final double[] signals = new double[actualMacds.length - periodSignal];
		if (actualMacds.length >= 9) {
			emaSignal.count(actualMacds, 0, signals);
		}
		
		// fill return table - (0) MACD, (1) EMA MACD
		final double[][] retTable = new double[2][];
		retTable[0] = actualMacds;
		retTable[1] = signals;
		
		return retTable;
	}
	
	
	/**
	 * CountMACD for one day
	 * @param prize - actual prize
	 * @param previousShort - previous short MACD
	 * @param previousLong - previous long MACD
	 * @param previousSignal - previous MACD
	 * @return
	 */
	public double[] single(final double prize, final double previousShort, final double previousLong, final double previousSignal) {
		
		// count single MACD and EMA MACD
		final double shortEma = emaShort.single(prize, previousShort);
		final double longEma = emaLong.single(prize, previousLong);
		final double macd = shortEma - longEma;
		final double signal = emaSignal.single(macd, previousSignal);
		
		// file return table: [0] MACD, [1] EMA MACD
		return new double[] { macd, signal };
	}
}
