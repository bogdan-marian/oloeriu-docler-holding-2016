package eu.oloeriu.interview.ping;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import eu.oloeriu.interview.App;
import eu.oloeriu.interview.App.Operation;
import eu.oloeriu.interview.models.ReportHandeler;

public class PingTcpIpProtocol implements Runnable{
	
	
	private String mHost;
	//: http://jasmin.com/en/
	private String mHostUrl;
	private int mTimeOut;
	private ReportHandeler mReportHandeler;
	
	private PingTcpIpProtocol(String host,int timeOut, ReportHandeler reportHandeler ){
		mHost = host;
		mHostUrl = "//http://"+mHost;
		mTimeOut = timeOut;
		mReportHandeler = reportHandeler;
	}
	
	public static PingTcpIpProtocol build (String host,int timeOut, ReportHandeler reportHandeler ){
		return new PingTcpIpProtocol(host, timeOut, reportHandeler);
	}
	
	public void runHttpGet(){
		try {
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(mTimeOut * 1000).build();
			HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
			//http://jasmin.com
			HttpGet httpGet = new HttpGet(mHostUrl);
			long startTime = System.nanoTime();
			HttpResponse httpResponse = httpClient.execute(httpGet);
			long endTime = System.nanoTime();
			long elapesedTime = endTime - startTime;
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			//long elapesedSecconds = TimeUnit.NANOSECONDS.toSeconds(elapesedTime);
			procesResponce(elapesedTime, statusCode);
		} catch (IOException e) {
			//just processResponce with custom values
			long elapesedTime = 0L;
			int statusCode = -1;
			procesResponce(elapesedTime,statusCode);
		}
	}
	
	/**
	 * wee only call the report error if the statusCode != 200 (not ok)
	 * this check satisfies all the requirements mentioned in the interview. 
	 * If status code is OK then wee just log the result
	 * @param elapesedTime
	 * @param statusCode
	 */
	private void procesResponce(long elapesedTime, int statusCode){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" Host url: "+ mHostUrl);
		stringBuilder.append(" / responceMilliseconds: " + elapesedTime);
		stringBuilder.append(" / httpStatusCode: "+ statusCode);
		
		App.Operation operation = Operation.PING_TCP;
		
		String result = stringBuilder.toString();
		if (statusCode == 200){
			mReportHandeler.updateLastItem(operation, mHost, result);
		}
		else{
			mReportHandeler.handleError(operation, mHost, result);
		}
	}

	@Override
	public void run() {
		runHttpGet();
	}
}
