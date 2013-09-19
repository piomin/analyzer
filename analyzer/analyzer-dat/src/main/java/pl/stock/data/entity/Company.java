package pl.stock.data.entity;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Index;

import pl.piomin.core.data.generic.GenericEntity;

@Entity
public class Company extends GenericEntity<Integer> {

	@Id
	@SequenceGenerator(name="company_pk_generator", sequenceName="company_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="company_pk_generator")
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	@Index(name = "company_symbol_ix")
	private String symbol;

	public Company() {
		
	}
	
	public Company(String name, String symbol) {
		this.name = name;
		this.symbol = symbol;
	}


	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Integer getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Integer pk) {
		this.id = pk;
	}

	/**
	 * Not used
	 */
	@Override
	public String getShortInfo() {
		return MessageFormat.format("{0}, {1}", id, symbol);
	}
	
}
