package pl.stock.web.ws.service;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;

import pl.stock.web.ws.protocol.Response;
import pl.stock.web.ws.protocol.Response.ResponseStatus;

@Service
public class ResponseProducerService {

	private final ResourceBundle BUNDLE = ResourceBundle.getBundle("pl.stock.ws.message");

	public Response createListResponse(List<?> list) {
		return new Response(list);
	}

	public Response createDataResponse(Object data) {
		return new Response(data);
	}

	public Response createErrorResponse(String errNo) {
		return new Response(ResponseStatus.ERROR, BUNDLE.getString(errNo), null);
	}
}
