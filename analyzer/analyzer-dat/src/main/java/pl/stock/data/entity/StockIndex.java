package pl.stock.data.entity;

import java.text.MessageFormat;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import pl.piomin.core.data.generic.GenericEntity;

@Entity
public class StockIndex extends GenericEntity<Integer> {

	@Id
	@SequenceGenerator(name = "index_pk_generator", sequenceName = "index_seq", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "index_pk_generator")
	private Integer id;

	private String name;

	@ManyToMany
	@Cascade(CascadeType.ALL)
	@JoinTable(name = "company_to_index", joinColumns = @JoinColumn(name = "index_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"))
	private List<Company> companies;

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

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	@Override
	public Integer getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Integer pk) {
		this.id = pk;
	}

	@Override
	public String getShortInfo() {
		return MessageFormat.format("{0}, {1}", id, name);
	}

}
