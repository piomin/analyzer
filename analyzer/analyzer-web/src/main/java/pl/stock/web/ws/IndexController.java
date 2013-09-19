package pl.stock.web.ws;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.stock.data.entity.StockIndex;
import pl.stock.data.service.StockIndexService;
import pl.stock.web.ws.protocol.Response;
import pl.stock.web.ws.service.ResponseProducerService;

@Controller
@Transactional(readOnly = true)
@RequestMapping("index")
public class IndexController {

	private static final Logger LOG = Logger.getLogger(IndexController.class);

	@Autowired
	private StockIndexService indexService;
	@Autowired
	private ResponseProducerService responseService;

	@RequestMapping("list")
	@ResponseBody
	public Response getList() {
		LOG.debug("GET list");
		List<StockIndex> indices = indexService.loadAll();
		return responseService.createListResponse(indices);
	}
	
}
