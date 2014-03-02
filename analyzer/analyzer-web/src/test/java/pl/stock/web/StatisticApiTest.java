package pl.stock.web;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import pl.stock.web.ws.protocol.Response;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StatisticApiTest {

	@Autowired
	private RestTemplate restTemplate;
	
	@Test
	public void testGroupLoad() {
		final String url = "http://localhost:8080/analyzer-web/api/stat/group/20130909/5";
		final Response response = restTemplate.getForObject(url, Response.class);
		Assert.assertTrue(response.getList().size() > 0);
	}
	
}
