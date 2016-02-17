package eu.oloeriu.interview.ping;

import eu.oloeriu.interview.App;

/**
 * Not a real test class but useful to start the app from eclipse. This is 
 * required now because mvn package is configured in pom to relocate some important 
 * resource files so they are read from the folder where the executable jar
 * resides. 
 * @author bogda
 *
 */
public class IntegrationUtilityA {
	public static void main(String[] args){
		App.main(args);
	}
}
