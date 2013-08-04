package pl.stock.data.dto;

import pl.stock.data.beans.SignalStatus;

public class StatisticRecordSimple {

	private double rsi;
	private double adx;
	private SignalStatus ema50Status;
	private SignalStatus macdStatus;
	private SignalStatus dmiStatus;
	private String companySymbol;

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

	public SignalStatus getEma50Status() {
		return ema50Status;
	}

	public void setEma50Status(SignalStatus ema50Status) {
		this.ema50Status = ema50Status;
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

}
