package pl.stock.data.dto;

import java.util.ArrayList;
import java.util.List;

public class StockIndex {

	private Integer id;
	private String name;
	private List<Company> companies = new ArrayList<>();

	public StockIndex() {

	}

	public StockIndex(pl.stock.data.entity.StockIndex index) {
		this.id = index.getId();
		this.name = index.getName();
		for (pl.stock.data.entity.Company company : index.getCompanies()) {
			this.companies.add(new Company(company));
		}
	}

	public StockIndex(Integer id, String name, List<Company> companies) {
		this.id = id;
		this.name = name;
		this.companies = companies;
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

	public List<Company> getCompanies() {
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

}
