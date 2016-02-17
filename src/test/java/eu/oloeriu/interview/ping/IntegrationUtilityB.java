package eu.oloeriu.interview.ping;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import eu.oloeriu.interview.App.Operation;
import eu.oloeriu.interview.models.ReportHandeler;

public class IntegrationUtilityB implements ReportHandeler {

	private static final String JASMINE = "jasmin.com";
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void main(String[] args) {
		System.out.println("Start of test");
		
		(new IntegrationUtilityB()).runTraceRt(JASMINE);
		
		System.out.println("End of test");
	}

	public void runTraceRt(String host) {
		TraceRt traceTask = TraceRt.build(host, this);

		final ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(traceTask, 0, 2, TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				scheduledFuture.cancel(true);
			}
		}, 60 * 5, TimeUnit.SECONDS);
	}

	@Override
	public void updateLastItem(Operation operation, String hostname, String resultLine) {
		System.out.println("Time to handle lastUpdate: "+ resultLine);

	}

	@Override
	public void handleError(Operation operation, String hostname, String resultLine) {
		System.out.println("Time to handle error");

	}
}
