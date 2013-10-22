package pl.stock.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.piomin.core.data.generic.GenericEntity;
import pl.stock.data.beans.UpdateStatus;
import pl.stock.data.beans.UpdateType;

@Entity
@Table(name = "update_history")
public class UpdateHistory extends GenericEntity<Integer> {
	
	@Id
	@SequenceGenerator(name="update_pk_generator", sequenceName="update_history_seq", initialValue=1, allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="update_pk_generator")
	private Integer id;

	@Temporal(TemporalType.DATE)
	@Column(name = "add_date")
	private Date addDate;
	
	@Column
	@Enumerated(EnumType.ORDINAL)
	private UpdateStatus status;

	@Column
	@Enumerated(EnumType.ORDINAL)
	private UpdateType type;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public UpdateStatus getStatus() {
		return status;
	}

	public void setStatus(UpdateStatus status) {
		this.status = status;
	}

	public UpdateType getType() {
		return type;
	}

	public void setType(UpdateType type) {
		this.type = type;
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
		return null;
	}
	
}
