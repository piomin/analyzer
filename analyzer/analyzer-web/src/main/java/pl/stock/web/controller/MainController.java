package pl.stock.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.service.CompanyService;
import pl.stock.data.service.DailyQuoteRecordService;

@Controller("controller") @ManagedBean(name = "controller")
@ViewScoped
@Transactional
public class MainController {

	@Autowired @ManagedProperty("#{recordService}")
	private DailyQuoteRecordService recordService;
	
	@Autowired @ManagedProperty("#{companyService}")
	private CompanyService companyService;
	
	private List<DailyQuoteRecord> records;

	
	public void setRecordService(DailyQuoteRecordService recordService) {
		this.recordService = recordService;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public List<DailyQuoteRecord> getRecords() {
		if (records == null) {
			records = new ArrayList<DailyQuoteRecord>();
			List<Company> companies = (List<Company>) companyService.loadAll();
			for (Company company : companies) {
				records.add(recordService.findLastByCompany(company));
			}
		}
		return records;
	}

	public void setRecords(List<DailyQuoteRecord> records) {
		this.records = records;
	}
	
}
