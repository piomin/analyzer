package pl.stock.data.entity;

import java.text.MessageFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Index;

import pl.piomin.core.data.generic.GenericEntity;

@Entity
@Table(name = "daily_quote_record", uniqueConstraints = @UniqueConstraint(columnNames = { "add_date", "company_id" }))
@org.hibernate.annotations.Table(appliesTo = "daily_quote_record", indexes = { @Index(name = "", columnNames = { "add_date", "company_id" }) })
public class DailyQuoteRecord extends GenericEntity<Long> {

	@Id
	@SequenceGenerator(name = "quote_pk_generator", sequenceName = "daily_quote_record_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quote_pk_generator")
	private Long id;

	@Index(name = "quote_date_ix")
	@Column(name = "add_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToOne(mappedBy = "quote")
	@JoinColumn(name = "id", referencedColumnName = "quote_id")
	@Cascade(CascadeType.DELETE)
	private StatisticRecord statistic;

	@Column(name = "min_prize")
	private double min;

	@Column(name = "max_prize")
	private double max;

	@Column(name = "open_prize")
	private double open;

	@Column(name = "close_prize")
	private double close;

	@Column
	private double volumen;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

	@Override
	public Long getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Long pk) {
		this.id = pk;
	}

	public StatisticRecord getStatistic() {
		return statistic;
	}

	public void setStatistic(StatisticRecord statistic) {
		this.statistic = statistic;
	}

	/**
	 * Not used
	 */
	@Override
	public String getShortInfo() {
		return MessageFormat.format("{0} [{1}; {2}; {3}; {4}]", company.getSymbol(), open, min, max, close);
	}

}
