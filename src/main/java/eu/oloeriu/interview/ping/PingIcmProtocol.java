package eu.oloeriu.interview.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import eu.oloeriu.interview.App.Operation;
import eu.oloeriu.interview.models.ReportHandeler;

public class PingIcmProtocol implements Runnable{
	final static Logger logger = Logger.getLogger(PingIcmProtocol.class);
	
	private List<String> mCommands;
	private ReportHandeler mReportHandeler;
	private String mHost;
	

	private PingIcmProtocol(String host, ReportHandeler reportHandeler) {
		String[] winCommands = { "ping", "-n", "5",host };
		mCommands = Arrays.asList(winCommands);
		mReportHandeler = reportHandeler;
		mHost = host;
	}
	
	public static PingIcmProtocol build(String host, ReportHandeler reportHandeler){
		return new PingIcmProtocol(host, reportHandeler);
	}

	public void ping() {
		ProcessBuilder prBuilder = new ProcessBuilder(mCommands);
		try {
			Process process = prBuilder.start();

			BufferedReader bufferdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader bufferdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line;
			
			//read output from command
			StringBuilder builderInput = new StringBuilder();
			while((line = bufferdInput.readLine())!= null){
				builderInput.append(line+"\n");
			}
			
			//read errors from command
			boolean hasErrors = false;
			StringBuilder builderError = new StringBuilder();
			while((line = bufferdError.readLine())!= null){
				hasErrors = true;
				builderError.append(line);
			}
			handleResult(hasErrors, builderInput.toString(), builderError.toString());
			
		} catch (IOException e) {
			throw new IllegalStateException("Not able to run IcmPing: " + mCommands);
		}
	}
	
	/**
	 * It uses the mReportHandleler member to process the ping results
	 * 
	 * @param hasErrors
	 * @param stdOut
	 * @param stdErr
	 */
	public void handleResult(boolean hasErrors, String stdOut, String stdErr){
		String line = stdOut + stdErr;
		if (hasErrors || containsLostItems(stdOut)){
			mReportHandeler.handleError(Operation.PING_ICMP, mHost, line);
			return;
		}
		mReportHandeler.updateLastItem(Operation.PING_ICMP, mHost, line);
	}
	
	/**
	 * I consider that it is sufficient to test if the ICM response contains
	 * lost items. Still there is allot of place to improve this logic. 
	 * @param stdOut
	 * @return
	 */
	public boolean containsLostItems (String stdOut){
		String[] parts = stdOut.split("Lost = ");
		if (parts.length < 2){
			return true;
		}
		String part = parts[1];
		String lost = part.split(" ")[0];
		if (lost.equals("0"))return false;
		else return true;
	}
	
	

	@Override
	public void run() {
		ping();
	}
}
