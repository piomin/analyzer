package pl.stock.logic.scheduler;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import pl.stock.data.entity.Company;
import pl.stock.data.entity.StockIndex;
import pl.stock.data.service.CompanyService;
import pl.stock.data.service.StockIndexService;
import pl.stock.data.service.UpdateHistoryService;
import pl.stock.logic.helper.IndicesListHelper;

@Service
@Transactional(readOnly = false)
@TransactionConfiguration(defaultRollback = false)
@PropertySource("classpath:pl/stock/logic/stock-logic.properties")
public class DownloadIndicesListScheduler {

	private final Logger LOGGER = Logger.getLogger(DownloadIndicesListScheduler.class);

	@Value("${scheduler.indices.active}")
	private boolean active;
	
	@Autowired
	private CompanyService companyService;

	@Autowired
	private StockIndexService indexService;

	@Autowired
	private UpdateHistoryService updateService;

	@Scheduled(cron = "0 0 */1 * * ?")
	public void schedule() {

		// do not run scheduler if is set to inactive
		if (!active) {
			return;
		}
		
		// do not process if there is no update
		if (updateService.count() == 0) {
			return;
		}

		LOGGER.info("Updating indices");
		
		// import all indices list
		try {
			List<String> indices = getAllIndices();
			for (String index : indices) {
				StockIndex indexEntity = getIndex(index);
				try {
					processIndexCompanies(indexEntity);
				} catch (IOException ex) {
					LOGGER.error("EXCEPTION.DOWNLOAD.CMPS." + index, ex);
				}
			}
		} catch (IOException e) {
			LOGGER.error("EXCEPTION.DOWNLOAD.IDXS", e);
		}
	}

	private void processIndexCompanies(StockIndex indexEntity) throws IOException {
		List<String> companies = getNamesByIndexType(indexEntity.getName().toLowerCase());
		for (String company : companies) {
			Company companyEntity = companyService.findBySymbol(company);
			if (companyEntity != null && !indexEntity.getCompanies().contains(company)) {
				indexEntity.getCompanies().add(companyEntity);
			}
		}
	}

	private List<String> getNamesByIndexType(String type) throws IOException {
		String url = MessageFormat.format("http://www.stockwatch.pl/gpw/indeks/{0},sklad.aspx", type);
		Document doc = IndicesListHelper.getDocument(url);
		return IndicesListHelper.findAllNames(doc, "StockIndex", "tbody", "td", "a");
	}

	private List<String> getAllIndices() throws IOException {
		Document doc = IndicesListHelper.getDocument("http://www.stockwatch.pl/stocks/");
		return IndicesListHelper.findAllNames(doc, "StockIndexes", "tbody", "td", "a");
	}
	
	private StockIndex getIndex(String name) {
		StockIndex indexEntity = indexService.findByName(name);
		if (indexEntity == null) {
			indexEntity = new StockIndex();
			indexEntity.setName(name);
			indexEntity.setCompanies(new ArrayList<Company>());
			indexService.add(indexEntity);
		}
		return indexEntity;
	}

}
