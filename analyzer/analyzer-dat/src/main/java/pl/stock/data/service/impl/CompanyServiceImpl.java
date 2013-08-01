package pl.stock.data.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.core.GenericServiceImpl;
import pl.stock.data.dao.CompanyDao;
import pl.stock.data.entity.Company;
import pl.stock.data.service.CompanyService;

@Service
@Transactional
public class CompanyServiceImpl extends GenericServiceImpl<Integer, Company> implements CompanyService {

	@Override
	public Company findBySymbol(String symbol) {
		return ((CompanyDao) dao).findBySymbol(symbol);
	}

	@Autowired
	public void setCompanyDao(CompanyDao companyDao) {
		this.dao = companyDao;
	}
	
}
