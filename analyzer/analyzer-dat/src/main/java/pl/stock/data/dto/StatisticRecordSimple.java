package pl.stock.data.dto;

import pl.stock.data.beans.SignalStatus;

public class StatisticRecordSimple {

	private double rsi;
	private double adx;
	private double sts;
	private SignalStatus ema50Status;
	private SignalStatus macdStatus;
	private SignalStatus dmiStatus;
	private String companySymbol;
	private Integer companyId;

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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

}
