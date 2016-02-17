package eu.oloeriu.interview.models;

public class ReportItem {
	private String host;
	private String icm_ping;
	private String tcp_ping;
	private String trace;
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getIcm_ping() {
		return icm_ping;
	}
	public void setIcm_ping(String icm_ping) {
		this.icm_ping = icm_ping;
	}
	public String getTcp_ping() {
		return tcp_ping;
	}
	public void setTcp_ping(String tcp_ping) {
		this.tcp_ping = tcp_ping;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	
	
}
