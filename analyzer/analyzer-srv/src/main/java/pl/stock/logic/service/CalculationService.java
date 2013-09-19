package pl.stock.logic.service;

import org.apache.commons.math.stat.StatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import pl.stock.algorithm.core.ATR;
import pl.stock.algorithm.core.DMI;
import pl.stock.algorithm.core.EMA;
import pl.stock.algorithm.core.MACD;
import pl.stock.algorithm.core.ROC;
import pl.stock.algorithm.core.RSI;
import pl.stock.algorithm.core.STS;

@Service
public class CalculationService {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private RSI rsi;

	@Autowired
	private STS sts;

	@Autowired
	private ATR atr;

	@Autowired
	private MACD macd;

	@Autowired
	private ROC roc;

	@Autowired
	private DMI dmi;

	private int quoteCount;

	public double[] countMultiEma(int period, double[] prizes) {
		if (prizes.length - period < 1) {
			return new double[0];
		}
		double[] emas = new double[prizes.length - period];
		EMA ema = (EMA) context.getBean("ema" + period);
		ema.count(prizes, 0, emas);
		return emas;
	}

	public double countSingleEma(int period, double prize, double previousEma) {
		if (period > quoteCount) {
			return 0;
		}
		EMA ema = (EMA) context.getBean("ema" + period);
		return ema.single(prize, previousEma);
	}

	public double countRsi(double[] prizes) {
		if (prizes.length < 15) {
			return 0;
		}
		return rsi.count(prizes)[0];
	}

	public double[] countMultiRsi(double[] prizes) {
		if (prizes.length < 15) {
			return new double[0];
		}
		return rsi.count(prizes);
	}

	public double[] countAdx(double[] closes, double[] mins, double[] maxs) {
		if (closes.length < 15) {
			return new double[0];
		}
		double[][] dmis = dmi.count(closes, mins, maxs);
		return new double[] { dmis[0][0], dmis[1][0], dmis[2][0] };
	}

	public double[][] countMultiAdx(double[] closes, double[] mins, double[] maxs) {
		if (closes.length < 14) {
			return new double[][] { new double[0], new double[0], new double[0] };
		}
		return dmi.count(closes, mins, maxs);
	}

	public double[] countSingleSts(double[] closes, double[] mins, double[] maxs, double previousStsSlow) {
		if (closes.length < 15) {
			return new double[0];
		}
		return sts.single(closes, mins, maxs, previousStsSlow);
	}

	public double[][] countMultiSts(double[] closes, double[] mins, double[] maxs) {
		if (closes.length < 15) {
			return new double[][] { new double[0], new double[0] };
		}
		return sts.count(closes, mins, maxs);
	}

	public double countSingleAtr(double closeYesterday, double min, double max, double previousAtr) {
		return atr.single(closeYesterday, min, max, previousAtr);
	}

	public double[] countMultiAtr(double closes[], double[] mins, double[] maxs) {
		if (closes.length < 16) {
			return new double[0];
		}
		return atr.count(closes, mins, maxs);
	}

	public double[] countSingleMacd(double prize, double previousShort, double previousLong, double previousSignal) {
		return macd.single(prize, previousShort, previousLong, previousSignal);
	}

	public double[][] countMultiMacd(double[] prizes) {
		if (prizes.length < 27) {
			return new double[][] { new double[0], new double[0] };
		}
		return macd.count(prizes);
	}

	public double countSma(int period, int offset, double[] prizes) {
		if (period > prizes.length) {
			return 0;
		}
		return StatUtils.mean(prizes, 0, period);
	}

	public double[] countMultiSma(int period, double[] prizes) {
		if (period > prizes.length) {
			return new double[0];
		}
		double[] smas = new double[prizes.length - period + 1];
		for (int i = 0; i < smas.length; i++) {
			countSma(period, i, prizes);
		}
		return smas;
	}

	public double countSingleRoc(double[] prizes) {
		if (prizes.length < 10) {
			return 0;
		}
		return roc.single(prizes);
	}

	public double[][] countMultiRoc(double[] prizes) {
		if (prizes.length < 10) {
			return new double[][] { new double[0], new double[0] };
		}
		return roc.count(prizes);
	}

	public int getQuoteCount() {
		return quoteCount;
	}

	public void setQuoteCount(int quoteCount) {
		this.quoteCount = quoteCount;
	}

}
