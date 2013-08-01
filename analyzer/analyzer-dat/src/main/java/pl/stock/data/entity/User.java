package pl.stock.data.entity;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import pl.stock.data.core.GenericEntity;

public class User extends GenericEntity<Integer> {

	@Id
	@SequenceGenerator(name="company_pk_generator", sequenceName="company_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="company_pk_generator")
	private Integer id;
	
	@Column
	private String email;
	
	@Column
	private String password;
	
	@Column
	private boolean active;
	
	@Override
	public Integer getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Integer pk) {
		id = pk;
	}

	@Override
	public String getShortInfo() {
		return MessageFormat.format("{0}, {1}", id, email);
	}

}
