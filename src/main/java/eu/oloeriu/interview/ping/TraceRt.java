package eu.oloeriu.interview.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import eu.oloeriu.interview.App.Operation;
import eu.oloeriu.interview.models.ReportHandeler;

public class TraceRt implements Runnable{
	
	private List<String> mCommands;
	private ReportHandeler mReportHandeler;
	private String mHost;
	
	private TraceRt(String host, ReportHandeler reportHandeler){
		mHost = host;
		String[] winCommands = {"tracert",mHost};
		mCommands = Arrays.asList(winCommands);
		mReportHandeler = reportHandeler;
	}
	
	public static TraceRt build(String host, ReportHandeler reportHandeler){
		return new TraceRt(host, reportHandeler);
	}
	
	public void trace(){
		ProcessBuilder processBuilder = new ProcessBuilder(mCommands);
		try {
			Process process = processBuilder.start();

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
	
	public void handleResult(boolean hasErrors, String stdOut, String stdErr){
		String line = stdOut + stdErr;
		
		mReportHandeler.updateLastItem(Operation.TRACE, mHost, line);
	}

	@Override
	public void run() {
		trace();
	}
}
