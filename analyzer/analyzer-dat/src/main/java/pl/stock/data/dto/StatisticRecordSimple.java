package pl.stock.data.dto;

import pl.stock.data.beans.SignalStatus;

public class StatisticRecordSimple {

	private double prize;
	private double rsi;
	private double adx;
	private double sts;
	private double roc;
	private double atr;
	private double atrPercentage;
	private double[] volumens;
	private double[] prizeChanges;
	private SignalStatus[] emasStatus;
	private double[] emasPercentage;
	private SignalStatus macdStatus;
	private SignalStatus dmiStatus;
	private String companySymbol;
	private Integer companyId;

	public double getPrize() {
		return prize;
	}

	public void setPrize(double prize) {
		this.prize = prize;
	}

	public double getRsi() {
		return rsi;
	}

	public void setRsi(double rsi) {
		this.rsi = rsi;
	}

	public double getAdx() {
		return adx;
	}

	public void setAdx(double adx) {
		this.adx = adx;
	}

	public double getSts() {
		return sts;
	}

	public void setSts(double sts) {
		this.sts = sts;
	}

	public double getRoc() {
		return roc;
	}

	public void setRoc(double roc) {
		this.roc = roc;
	}

	public double getAtr() {
		return atr;
	}

	public void setAtr(double atr) {
		this.atr = atr;
	}

	public double getAtrPercentage() {
		return atrPercentage;
	}

	public void setAtrPercentage(double atrPercentage) {
		this.atrPercentage = atrPercentage;
	}

	public double[] getVolumens() {
		return volumens;
	}

	public void setVolumens(double[] volumens) {
		this.volumens = volumens;
	}

	public void addVolumen(int i, double volumen) {
		this.volumens[i] = volumen;
	}

	public double[] getPrizeChanges() {
		return prizeChanges;
	}

	public void setPrizeChanges(double[] prizeChanges) {
		this.prizeChanges = prizeChanges;
	}

	public void addPrizeChange(int i, double prize) {
		this.prizeChanges[i] = prize;
	}

	public SignalStatus[] getEmasStatus() {
		return emasStatus;
	}

	public void setEmasStatus(SignalStatus[] emasStatus) {
		this.emasStatus = emasStatus;
	}

	public void addEmaStatus(int i, SignalStatus status) {
		this.emasStatus[i] = status;
	}

	public double[] getEmasPercentage() {
		return emasPercentage;
	}

	public void setEmasPercentage(double[] emasValue) {
		this.emasPercentage = emasValue;
	}

	public void addEmaPercentage(int i, double emaValue) {
		this.emasPercentage[i] = emaValue;
	}

	public SignalStatus getMacdStatus() {
		return macdStatus;
	}

	public void setMacdStatus(SignalStatus macdStatus) {
		this.macdStatus = macdStatus;
	}

	public SignalStatus getDmiStatus() {
		return dmiStatus;
	}

	public void setDmiStatus(SignalStatus dmiStatus) {
		this.dmiStatus = dmiStatus;
	}

	public String getCompanySymbol() {
		return companySymbol;
	}

	public void setCompanySymbol(String companySymbol) {
		this.companySymbol = companySymbol;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
