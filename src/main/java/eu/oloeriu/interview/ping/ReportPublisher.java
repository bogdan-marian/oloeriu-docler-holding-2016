package eu.oloeriu.interview.ping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

public class ReportPublisher implements Runnable{
	final static Logger logger = Logger.getLogger(ReportPublisher.class);
	
	private String mHostUrl;
	private String mReportJson;
	
	private ReportPublisher(String hostUrl, String report){
		mHostUrl = hostUrl;
		mReportJson = report;
	}
	
	public static ReportPublisher build(String host, String report){
		return new ReportPublisher(host, report);
	}
	
	public void publishReport(){
		logger.info("Hello from publisher");
		
		RequestConfig requestConfig = RequestConfig.custom().build();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		HttpPost httpPost = new HttpPost(mHostUrl);
		
		try {
			StringEntity jsonEntity = new StringEntity(mReportJson);
			jsonEntity.setContentType("application/json");
			httpPost.setEntity(jsonEntity);
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200){
				logger.info("Report publisher: strange statusCode = "+ statusCode);
			}
		} catch (UnsupportedEncodingException e) {
			logger.info("Report publisher: UnsupportedEncodingException"+e);
		} catch (ClientProtocolException e) {
			logger.info("Report publisher: ClientProtocolException"+e);
		} catch (IOException e) {
			logger.info("Report publisher: IOException"+e);
		}
	}

	@Override
	public void run() {
		publishReport();
	}
}
