package eu.oloeriu.interview;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

public class BasicTest extends TestCase {
	
	final static Logger logger = Logger.getLogger(App.class);

	/**
	 * it is more a snippet then a test
	 */
	public void test1ScheduleWithFixedDelay() {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

		Runnable task = () -> {
			try {
				TimeUnit.SECONDS.sleep(2);
				System.out.println("Scheduling: " + System.nanoTime());
			} catch (InterruptedException e) {
				System.err.println("task interrupted");
			}
		};

		executor.scheduleWithFixedDelay(task, 0, 1, TimeUnit.SECONDS);
	}

	public void test2SendHttpRequest() {
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).build();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			//http://jasmin.com/en/
			String url = "http://jasmin.com/";
			HttpGet httpGet = new HttpGet(url);
			long startTime = System.nanoTime();
			logger.info("Start time: " + startTime);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			long endTime = System.nanoTime();
			long elapesedTime = endTime - startTime;
			logger.info("End time: " + endTime);
			logger.info("Elapesed time: "+ elapesedTime);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			long elapesedSecconds = TimeUnit.NANOSECONDS.toSeconds(elapesedTime);
			logger.info("Status code = " + statusCode +" / " +elapesedTime+" / " + elapesedSecconds);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
