package eu.oloeriu.interview.models;

import java.util.List;

public class Settings {
	private int delayInSeconds;
	private int tcpIpTimeOut;
	private List<String> hosts;
	private String reportingUrl;

	public List<String> getHosts() {
		return hosts;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

	public int getDelayInSeconds() {
		return delayInSeconds;
	}

	public void setDelayInSeconds(int delayInSeconds) {
		this.delayInSeconds = delayInSeconds;
	}

	public int getTcpIpTimeOut() {
		return tcpIpTimeOut;
	}

	public void setTcpIpTimeOut(int tcpIpTimeOut) {
		this.tcpIpTimeOut = tcpIpTimeOut;
	}

	public String getReportingUrl() {
		return reportingUrl;
	}

	public void setReportingUrl(String reportingUrl) {
		this.reportingUrl = reportingUrl;
	}
	
	
}
