package pl.stock.data.dto;

import java.util.Date;

public class StatisticRecord {

	private double rsi;
	private double ema5;
	private double ema10;
	private double ema20;
	private double ema50;
	private double ema100;
	private int averageVol5;
	private int averageVol12;
	private int averageVol26;
	private int averageVol50;
	private double roc;
	private double sroc;
	private double sts;
	private double stsEma;
	private double dmiPlus;
	private double dmiMinus;
	private double adx;
	private double macd;
	private double macdEma;
	private double atr;
	private double ema12;
	private double ema14;
	private double ema26;
	private double sma14;
	private double sma28;
	private double sma42;
	private Date addDate;

	public StatisticRecord(pl.stock.data.entity.StatisticRecord statistic) {
		this.rsi = statistic.getRsi();
		this.ema5 = statistic.getEma5();
		this.ema10 = statistic.getEma10();
		this.ema20 = statistic.getEma20();
		this.ema50 = statistic.getEma50();
		this.ema100 = statistic.getEma100();
		this.averageVol5 = statistic.getAverageVol5();
		this.averageVol12 = statistic.getAverageVol12();
		this.averageVol26 = statistic.getAverageVol26();
		this.averageVol50 = statistic.getAverageVol50();
		this.roc = statistic.getRoc();
		this.sroc = statistic.getSroc();
		this.sts = statistic.getSts();
		this.stsEma = statistic.getStsEma();
		this.dmiPlus = statistic.getDmiPlus();
		this.dmiMinus = statistic.getDmiMinus();
		this.adx = statistic.getAdx();
		this.macd = statistic.getMacd();
		this.macdEma = statistic.getMacdEma();
		this.atr = statistic.getAtr();
		this.ema12 = statistic.getEma12();
		this.ema14 = statistic.getEma14();
		this.ema26 = statistic.getEma26();
		this.sma14 = statistic.getSma14();
		this.sma28 = statistic.getSma28();
		this.sma42 = statistic.getSma42();
		this.addDate = statistic.getAddDate();
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	public double getEma5() {
		return ema5;
	}

	public void setEma5(double ema5) {
		this.ema5 = ema5;
	}

	public double getEma10() {
		return ema10;
	}

	public void setEma10(double ema10) {
		this.ema10 = ema10;
	}

	public double getEma20() {
		return ema20;
	}

	public void setEma20(double ema20) {
		this.ema20 = ema20;
	}

	public double getEma50() {
		return ema50;
	}

	public void setEma50(double ema50) {
		this.ema50 = ema50;
	}

	public double getEma100() {
		return ema100;
	}

	public void setEma100(double ema100) {
		this.ema100 = ema100;
	}

	public int getAverageVol5() {
		return averageVol5;
	}

	public void setAverageVol5(int averageVol5) {
		this.averageVol5 = averageVol5;
	}

	public int getAverageVol12() {
		return averageVol12;
	}

	public void setAverageVol12(int averageVol12) {
		this.averageVol12 = averageVol12;
	}

	public int getAverageVol26() {
		return averageVol26;
	}

	public void setAverageVol26(int averageVol26) {
		this.averageVol26 = averageVol26;
	}

	public int getAverageVol50() {
		return averageVol50;
	}

	public void setAverageVol50(int averageVol50) {
		this.averageVol50 = averageVol50;
	}

	public double getRoc() {
		return roc;
	}

	public void setRoc(double roc) {
		this.roc = roc;
	}

	public double getSroc() {
		return sroc;
	}

	public void setSroc(double sroc) {
		this.sroc = sroc;
	}

	public double getSts() {
		return sts;
	}

	public void setSts(double sts) {
		this.sts = sts;
	}

	public double getStsEma() {
		return stsEma;
	}

	public void setStsEma(double stsEma) {
		this.stsEma = stsEma;
	}

	public double getDmiPlus() {
		return dmiPlus;
	}

	public void setDmiPlus(double dmiPlus) {
		this.dmiPlus = dmiPlus;
	}

	public double getDmiMinus() {
		return dmiMinus;
	}

	public void setDmiMinus(double dmiMinus) {
		this.dmiMinus = dmiMinus;
	}

	public double getAdx() {
		return adx;
	}

	public void setAdx(double adx) {
		this.adx = adx;
	}

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}

	public double getMacdEma() {
		return macdEma;
	}

	public void setMacdEma(double macdEma) {
		this.macdEma = macdEma;
	}

	public double getAtr() {
		return atr;
	}

	public void setAtr(double atr) {
		this.atr = atr;
	}

	public double getEma12() {
		return ema12;
	}

	public void setEma12(double ema12) {
		this.ema12 = ema12;
	}

	public double getEma14() {
		return ema14;
	}

	public void setEma14(double ema14) {
		this.ema14 = ema14;
	}

	public double getEma26() {
		return ema26;
	}

	public void setEma26(double ema26) {
		this.ema26 = ema26;
	}

	public double getSma14() {
		return sma14;
	}

	public void setSma14(double sma14) {
		this.sma14 = sma14;
	}

	public double getSma28() {
		return sma28;
	}

	public void setSma28(double sma28) {
		this.sma28 = sma28;
	}

	public double getSma42() {
		return sma42;
	}

	public void setSma42(double sma42) {
		this.sma42 = sma42;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

}
