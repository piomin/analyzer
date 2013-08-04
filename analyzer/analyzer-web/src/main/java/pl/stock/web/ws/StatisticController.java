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

import pl.stock.data.dto.StatisticRecordSimple;
import pl.stock.data.service.StatisticRecordService;
import pl.stock.logic.service.StockLogicService;

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

    @RequestMapping("list/{date}")
    @ResponseBody
    public List<StatisticRecordSimple> getListByDate(@PathVariable String date) {
	LOG.debug(MessageFormat.format("GET list/{0}", date));
	try {
	    return logicService.processStatisticListRequest(DF.parse(date, LOC));
	} catch (ParseException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
}
