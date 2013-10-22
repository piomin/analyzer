package pl.stock.web.ws;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.stock.data.dto.StatisticDetails;
import pl.stock.data.dto.StatisticRecordSimple;
import pl.stock.data.service.StatisticRecordService;
import pl.stock.logic.service.StockLogicService;
import pl.stock.web.ws.protocol.Response;
import pl.stock.web.ws.service.ResponseProducerService;

@Controller
@Transactional(readOnly = true)
@RequestMapping("stat")
public class StatisticController {

	private static final Logger LOG = Logger.getLogger(StatisticController.class);
	private static final DateFormatter DF = new DateFormatter("yyyyMMdd");
	private static final Locale LOC = new Locale("pl");

	@Autowired
	private StatisticRecordService statisticService;

	@Autowired
	private StockLogicService logicService;

	@Autowired
	private ResponseProducerService responseService;

	@RequestMapping("list/{date}")
	@ResponseBody
	public Response getListByDate(@PathVariable String date) {
		LOG.debug(MessageFormat.format("GET list/{0}", date));
		try {
			List<StatisticRecordSimple> statistics = logicService.processStatisticListRequest(DF.parse(date, LOC));
			return responseService.createListResponse(statistics);
		} catch (ParseException e) {
			LOG.error("Error parsing input", e);
			return responseService.createErrorResponse("err.invalid.request");
		}
	}

	@RequestMapping("list/{date}/{ids}")
	@ResponseBody
	public Response getListByDateAndIds(@PathVariable String date, @PathVariable String ids) {
		LOG.debug(MessageFormat.format("GET list/{0}/{1}", date, ids));
		try {
			List<StatisticRecordSimple> statistics = logicService.processStatisticListRequest(DF.parse(date, LOC), ids);
			return responseService.createListResponse(statistics);
		} catch (ParseException e) {
			LOG.error("Error parsing input", e);
			return responseService.createErrorResponse("err.invalid.request");
		}
	}
	
	@RequestMapping("group/{date}/{id}")
	@ResponseBody
	public Response getListByDateAndGroup(@PathVariable String date, @PathVariable String id) {
		LOG.debug(MessageFormat.format("GET group/{0}/{1}", date, id));
		try {
			List<StatisticRecordSimple> statistics = logicService.processGroupRequest(DF.parse(date, LOC), Integer.valueOf(id));
			return responseService.createListResponse(statistics);
		} catch (ParseException e) {
			LOG.error("Error parsing input", e);
			return responseService.createErrorResponse("err.invalid.request");
		}
	}

	@RequestMapping("details/{date}/{companyId}")
	@ResponseBody
	public Response getDetails(@PathVariable String date, @PathVariable Integer companyId) {
		LOG.debug(MessageFormat.format("GET details/{0}/{1}", date, companyId));
		try {
			StatisticDetails statistic = logicService.processDetailsRequest(DF.parse(date, LOC), companyId);
			return responseService.createDataResponse(statistic);
		} catch (ParseException e) {
			LOG.error("Error parsing input", e);
			return responseService.createErrorResponse("err.invalid.request");
		}
	}

}
