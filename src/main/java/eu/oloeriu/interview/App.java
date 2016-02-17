package eu.oloeriu.interview;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.oloeriu.interview.models.ReportHandeler;
import eu.oloeriu.interview.models.ReportItem;
import eu.oloeriu.interview.models.Settings;
import eu.oloeriu.interview.ping.PingIcmProtocol;
import eu.oloeriu.interview.ping.PingTcpIpProtocol;
import eu.oloeriu.interview.ping.ReportPublisher;
import eu.oloeriu.interview.ping.TraceRt;


public class App implements ReportHandeler {
	public enum Operation {
		PING_ICMP, PING_TCP, TRACE
	}

	final static Logger logger = Logger.getLogger(App.class);

	private static final String settingsFile = "/Settings.json";
	private static final Gson gson = new GsonBuilder().create();
	private static Map<String, ReportItem> map = new ConcurrentHashMap<>();

	private Settings mSettings;

	private void setmSettings(Settings mSettings) {
		this.mSettings = mSettings;
	}

	public static void main(String[] args) {
		InputStream inputStream = App.class.getResourceAsStream(settingsFile);
		try {
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			Settings settings = gson.fromJson(reader, Settings.class);

			App app = new App();
			app.setmSettings(settings);

			List<String> hosts = settings.getHosts();
			// String host = hosts.get(0);
			// app.pingIcm(host);
			for (String host : hosts) {
				// initiate threads
				// app.pingIcm(host,settings.getDelayInSeconds());
				// app.runTraceRt(host,settings.getDelayInSeconds());
				app.runPingTcp(host, settings.getTcpIpTimeOut(), settings.getDelayInSeconds());
			}

			logger.info("All threads started: Time to enjoy a coffee");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Not able to read from " + settingsFile);
		}
	}

	/**
	 * Stores and logs the last operation associated with a specific host
	 * 
	 * @param operation
	 * @param hostname
	 * @param resultLine
	 * @return the updated Report item that contains all last lines for the
	 *         specific host.
	 */
	private ReportItem getItemAndUpdate(Operation operation, String hostname, String resultLine) {
		ReportItem reportItem = map.get(hostname);
		if (reportItem == null) {
			reportItem = new ReportItem();
			reportItem.setHost(hostname);
		}
		switch (operation) {
		case PING_ICMP:
			reportItem.setIcm_ping(resultLine);
			break;
		case PING_TCP:
			reportItem.setTcp_ping(resultLine);
			break;
		case TRACE:
			reportItem.setTrace(resultLine);
			break;
		}
		map.put(hostname, reportItem);

		logger.info(resultLine);
		return reportItem;
	}

	@Override
	public void updateLastItem(Operation operation, String hostname, String resultLine) {
		getItemAndUpdate(operation, hostname, resultLine);
	}

	@Override
	public void handleError(Operation operation, String hostname, String resultLine) {
		ReportItem reportItem = getItemAndUpdate(operation, hostname, resultLine);
		sendReport(reportItem);
	}

	/**
	 * Because wee have many threads that might decide to send reports it is
	 * better if wee make also sending a report a thread
	 * @param reportItem
	 */
	private void sendReport(ReportItem reportItem) {
		if(reportItem.getIcm_ping()==null)reportItem.setIcm_ping("no history");
		if(reportItem.getTcp_ping()==null)reportItem.setTcp_ping("no history");
		if(reportItem.getTrace()==null)reportItem.setTrace("no history");
		String reportJson = gson.toJson(reportItem);
		String publishUrl = mSettings.getReportingUrl();
		ReportPublisher publisher = ReportPublisher.build(publishUrl, reportJson);
		Thread thread = new Thread(publisher);
		thread.start();
	}

	public void pingIcm(String host, int delayInSeconds) {
		PingIcmProtocol pingTask = PingIcmProtocol.build(host, this);
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		final ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(pingTask, 0, delayInSeconds,
				TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				scheduledFuture.cancel(true);
			}
		}, 60 * 60, TimeUnit.SECONDS);
	}

	public void runTraceRt(String host, int delayInSeconds) {
		TraceRt traceTask = TraceRt.build(host, this);
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		final ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(traceTask, 0, delayInSeconds,
				TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				scheduledFuture.cancel(true);
			}
		}, 60 * 60, TimeUnit.SECONDS);
	}

	public void runPingTcp(String host, int timeOut, int delayInSeconds) {

		PingTcpIpProtocol pingTcp = PingTcpIpProtocol.build(host, timeOut, this);
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		final ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(pingTcp, 0, delayInSeconds,
				TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				scheduledFuture.cancel(true);
			}
		}, 60 * 60, TimeUnit.SECONDS);
	}
}
