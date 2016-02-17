package eu.oloeriu.interview.ping;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import eu.oloeriu.interview.App.Operation;
import eu.oloeriu.interview.models.ReportHandeler;

public class IntegrationUtilityC implements ReportHandeler {

	private static final String JASMINE = "jasmin.com";
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public static void main(String[] args) {
		System.out.println("Start of test");
		
		(new IntegrationUtilityC()).runPingTcp(JASMINE);
		
		System.out.println("End of test");
	}

	public void runPingTcp(String host) {
		PingTcpIpProtocol pingTcp = PingTcpIpProtocol.build(host, 5, this);

		final ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(pingTcp, 0, 2, TimeUnit.SECONDS);
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
		System.out.println("Time to handle error "+ resultLine);

	}
}
