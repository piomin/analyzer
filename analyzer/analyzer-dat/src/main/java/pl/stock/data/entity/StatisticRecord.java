package pl.stock.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.piomin.core.data.generic.GenericEntity;

@Entity
@Table(name = "statistic_record")
public class StatisticRecord extends GenericEntity<Long> {

	@Id
	@SequenceGenerator(name="statistic_pk_generator", sequenceName="statistic_record_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="statistic_pk_generator")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "quote_id")
	private DailyQuoteRecord quote;
	
	@Column
	private double rsi;
	
	@Column
	private double ema5;
	
	@Column
	private double ema10;
	
	@Column
	private double ema20;
	
	@Column
	private double ema50;
	
	@Column
	private double ema100;
	
	@Column(name = "average_vol5")
	private int averageVol5;
	
	@Column(name = "average_vol12")
	private int averageVol12;
	
	@Column(name = "average_vol26")
	private int averageVol26;
	
	@Column(name = "average_vol50")
	private int averageVol50;
	
	@Column
	private double roc;
	
	@Column
	private double sroc;
	
	@Column
	private double sts;
	
	@Column(name = "sts_ema")
	private double stsEma;
	
	@Column(name = "dmi_plus")
	private double dmiPlus;
	
	@Column(name = "dmi_minus")
	private double dmiMinus;
	
	@Column
	private double adx;
	
	@Column
	private double macd;
	
	@Column(name = "macd_ema")
	private double macdEma;
	
	@Column
	private double atr;
	
	@Column
	private double ema12;
	
	@Column
	private double ema14;
	
	@Column
	private double ema26;
	
	@Column
	private double sma14;
	
	@Column
	private double sma28;
	
	@Column
	private double sma42;

	@Temporal(TemporalType.DATE)
	@Column(name = "add_date")
	private Date addDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DailyQuoteRecord getQuote() {
		return quote;
	}

	public void setQuote(DailyQuoteRecord quote) {
		this.quote = quote;
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

	@Override
	public Long getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Long pk) {
		this.id = pk;
	}

	/**
	 * Not used
	 */
	@Override
	public String getShortInfo() {
		return null;
	}
	
}
