package pl.stock.data.dto;

import java.util.Date;

public class DailyQuoteRecord {

	private Date date;
	private double min;
	private double max;
	private double open;
	private double close;
	private double volumen;
	private String companySymbol;

	public DailyQuoteRecord(pl.stock.data.entity.DailyQuoteRecord entity) {
		this.date = entity.getDate();
		this.min = entity.getMin();
		this.max = entity.getMax();
		this.open = entity.getOpen();
		this.close = entity.getClose();
		this.volumen = entity.getVolumen();
		this.companySymbol = entity.getCompany().getSymbol();
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getVolumen() {
		return volumen;
	}

	public void setVolumen(double volumen) {
		this.volumen = volumen;
	}

	public String getCompanySymbol() {
		return companySymbol;
	}

	public void setCompanySymbol(String companySymbol) {
		this.companySymbol = companySymbol;
	}

}
