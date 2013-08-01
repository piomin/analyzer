package pl.stock.algorithm.core;

/**
 * Class for calculating Directional Movement Index (DMI)
 * @author Piotr Mińkowski
 *
 */
public class DMI {

	// period for count DMI
	private int period;
	
	// EMA for DMI
	private EMA ema;
	
	// ATR for DMI
	private ATR atr;
	
	/**
	 * Constructor
	 * @param period - set period
	 */
	public DMI(final int period) {
		this.period = period;
		this.ema = new EMA(2 * period);
		this.atr = new ATR(period);
	}
	
	
	/**
	 * Calculating Directional Movement Index (DMI)
	 * @param closes - close prizes for specified period
	 * @param mins - minimum prizes per day for specified period 
	 * @param maxs - maximum prizes per day for specified period
	 * @return - DMI table
	 */
	public double[][] count(double[] closes, double[] mins, double[] maxs) {
		
		// count DM+ and DM- for each day during specified period
		double dmPlus, dmMinus;
		final int dmLength = closes.length - 1;
		final double[] dmPluses = new double[dmLength];
		final double[] dmMinuses = new double[dmLength];
		for (int i = 0; i < dmLength; i++) {
			dmPlus = maxs[i] - maxs[i+1];
			dmMinus = mins[i+1] - mins[i];
			if (dmPlus > 0 && dmPlus > dmMinus || dmMinus < 0) {
				dmMinus = 0;
			}
			if(dmMinus > 0 && dmMinus > dmPlus || dmPlus < 0) {
				dmPlus = 0;
			}
			if (dmPlus == dmMinus) {
				if (closes[i] < closes[i+1]) {
					dmPlus = 0;
				} else {
					dmMinus = 0;
				}
			}
			dmPluses[i] = dmPlus;
			dmMinuses[i] = dmMinus;
		}
		
		// count ATR for closes, minimum and maximum tables
		final int emasLength = closes.length - 1 - 2 * period;
		final double[] emaPlusesTab = new double[emasLength];
		final double[] emaMinusesTab = new double[emasLength];
		final double[] emaTrs = atr.count(closes, mins, maxs);
		ema.count(dmPluses, 0, emaPlusesTab);
		ema.count(dmMinuses, 0, emaMinusesTab);

		// count DI ratio for DM+ and DM-, ratio are EMA(DM+)/EMA(ATR) and EMA(DM-)/EMA(ATR) 
		final double[] dips = new double[emasLength];
		final double[] dims = new double[emasLength];
		final double[] dxs = new double[emasLength];
		for (int i = 0; i < dxs.length; i++) {
			double diPlus = emaPlusesTab[i] / emaTrs[i];
			double diMinus = emaMinusesTab[i] / emaTrs[i];
			dips[i] = 100 * diPlus;
			dims[i] = 100 * diMinus;
			dxs[i] = 100 * Math.abs(diPlus - diMinus) / (diPlus + diMinus);
		}
		
		// count ADX as EMA for DI ratio table
		final double[] adxs = new double[emasLength - 2 * period]; 
		ema.count(dxs, 0, adxs);
		
		// fill return table - [0] EMA DM+ table, [1] EMA DM- table, [2] ADX table
		double[][] retTable = new double[3][];
		retTable[0] = dips;
		retTable[1] = dims;
		retTable[2] = adxs;
		
		return retTable;
	}
}
