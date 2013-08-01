package pl.stock.web.ws;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.stock.data.entity.Company;
import pl.stock.data.entity.DailyQuoteRecord;
import pl.stock.data.service.CompanyService;
import pl.stock.data.service.DailyQuoteRecordService;

@Controller
@Transactional(readOnly = true)
@RequestMapping("quote")
public class DailyQuoteController {

	private static final Logger LOG = Logger.getLogger(DailyQuoteController.class);
	
	@Autowired
	private DailyQuoteRecordService quoteService;
	@Autowired
	private CompanyService companyService;
	
	@RequestMapping("company/{id}")
	@ResponseBody
	public List<pl.stock.data.dto.DailyQuoteRecord> getByCompany(@PathVariable Integer id) {
		LOG.debug(MessageFormat.format("GET api/quote/company/{0}", id));
		Company company = companyService.load(id);
		List<DailyQuoteRecord> quotes = quoteService.findByCompany(company, 50);
		List<pl.stock.data.dto.DailyQuoteRecord> quoteDTOs = new ArrayList<>();
		for (DailyQuoteRecord quote : quotes) {
			quoteDTOs.add(new pl.stock.data.dto.DailyQuoteRecord(quote));
		}
		return quoteDTOs;
	}
	
	@RequestMapping("company/{id}/{limit}")
	@ResponseBody
	public List<DailyQuoteRecord> getByCompanyWithLimit(@PathVariable Integer id, @PathVariable Integer limit) {
		LOG.debug(MessageFormat.format("GET api/quote/company/{0}/{1}", id, limit));
		Company company = companyService.load(id);
		return quoteService.findByCompany(company, limit);
	}
	
	@RequestMapping("all/date/{date}")
	@ResponseBody
	public List<DailyQuoteRecord> getAllByDate(@PathVariable Date date) {
		LOG.debug(MessageFormat.format("GET all/date/{0}", date));
		// TODO - implement search by date method
		return null;
	}
	
	@RequestMapping("all/date/{date}/{limit}/{offset}")
	@ResponseBody
	public List<DailyQuoteRecord> getAllByDate(@PathVariable Date date, @PathVariable Integer limit, @PathVariable Integer offset) {
		LOG.debug(MessageFormat.format("GET all/date/{0}/{1}/{2}", date, limit, offset));
		// TODO - implement search by date method with pagination
		return null;
	}
	
}
