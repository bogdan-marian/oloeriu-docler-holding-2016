package eu.oloeriu.interview.models;

import eu.oloeriu.interview.App.Operation;

/**
 * callback interface that will collect the data for the last operation
 * @author Bogdan
 */
public interface ReportHandeler {
	public void updateLastItem(Operation operation, String hostname, String resultLine);
	public void handleError(Operation operation, String hostname, String resultLine);
}
