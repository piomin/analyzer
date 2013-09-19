package pl.stock.web.ws.protocol;

import java.io.Serializable;
import java.util.Collection;

public class Response implements Serializable {

	public static final String VERSION = "0.1";

	public enum ResponseStatus {
		OK, ERROR, WARNING;
	}

	private String version = VERSION;
	private ResponseStatus status = ResponseStatus.OK;
	private String details;
	private Object data;
	private Collection<?> list;

	public Response() {

	}

	public Response(String version, ResponseStatus status, String details, Object data) {
		this.version = version;
		this.status = status;
		this.details = details;
		this.data = data;
	}

	public Response(String version, ResponseStatus status, String details, Collection<?> list) {
		this.version = version;
		this.status = status;
		this.details = details;
		this.list = list;
	}

	public Response(ResponseStatus status, String details, Object data) {
		this(VERSION, status, details, data);
	}

	public Response(ResponseStatus status, String details, Collection<?> list) {
		this(VERSION, status, details, list);
	}

	public Response(Object data) {
		this.data = data;
	}

	public Response(Collection<?> list) {
		this.list = list;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public void setStatus(ResponseStatus status) {
		this.status = status;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Collection<?> getList() {
		return list;
	}

	public void setList(Collection<?> list) {
		this.list = list;
	}

}
