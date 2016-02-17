package eu.oloeriu.interview.ping;

import eu.oloeriu.interview.App.Operation;
import eu.oloeriu.interview.models.ReportHandeler;
import junit.framework.TestCase;

public class PingIcmProtocolTest extends TestCase implements ReportHandeler{
	private String jasmine = "jasmin.com";
	
	public void test1Jasmin(){
		PingIcmProtocol.build(jasmine,this).ping();
	}

	@Override
	public void updateLastItem(Operation operation, String hostname, String resultLine) {
		//not important for this test
	}

	@Override
	public void handleError(Operation operation, String hostname, String resultLine) {
		//not important for this test
	}
}
