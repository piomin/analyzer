package pl.stock.logic.helper;

import pl.stock.data.beans.SignalStatus;

public final class CalculationHelper {

	/**
	 * Signal status detection
	 * @param positive - values for positive event (faster data)
	 * @param negative - values for negative event (slower data)
	 * @param warnRatio - warning ratio level
	 * @return signal status
	 */
	public static final SignalStatus detectSignalType(double[] positive, double[] negative, double warnRatio) {
		double diff = positive[0] - negative[0];
		double prevDiff = positive[1] - negative[1];
		// check if positive and negative lines intersected
		if (diff*prevDiff < 0) {
			if (diff >= 0) return SignalStatus.POSITIVE; // if actual positive is bigger than negative signal is positive
			else return SignalStatus.NEGATIVE;
		}
		// check if intersection is close enough to send warning signal
		if (Math.abs(diff) < Math.abs(prevDiff) && diff/negative[0] < warnRatio) {
			if (diff > 0) return SignalStatus.WARNING_PLUS;
			else return SignalStatus.WARNING_MINUS;
		}
		// there was no signal
		return SignalStatus.NONE;
	}
}
