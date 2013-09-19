package pl.stock.data.dto;

public class Company {

	private Integer id;
	private String name;
	private String symbol;

	public Company() {

	}

	public Company(pl.stock.data.entity.Company company) {
		this.id = company.getId();
		this.name = company.getName();
		this.symbol = company.getSymbol();
	}

	public Company(Integer id, String name, String symbol) {
		this.id = id;
		this.name = name;
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

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
