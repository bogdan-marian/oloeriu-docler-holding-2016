package eu.oloeriu.interview.models;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.TestCase;

public class SettingsTest extends TestCase {
	
	String mSettingsFile = "Settings.json";
	String jasmine = "jasmin.com";
	
	public void test1BuildAndSaveSettings(){
		Settings settings = new Settings();
		
		
		try {
			Writer writer = new FileWriter(mSettingsFile);
			Gson gson = new GsonBuilder().create();
			gson.toJson(settings, writer);
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException("Not able to write to file");
		}
	}
	
	public void test2LoadSettings(){
		InputStream inputStream = SettingsTest.class.getResourceAsStream("/"+mSettingsFile);
		try {
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
			Gson gson = new GsonBuilder().create();
			Settings settings = gson.fromJson(reader, Settings.class);
			assertTrue("Not the expected host items", settings.getHosts().size()==2);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Not able to read from " + mSettingsFile);
		}
	}
	
	/**
	 * this one uses arrays
	 */
	public void test3BuildAndSaveSettings(){
		Settings settings = new Settings();
		
		String[] names = {"jasmin.com", "oranum.com"};
		settings.setHosts(Arrays.asList(names));
		settings.setDelayInSeconds(2);
		settings.setTcpIpTimeOut(5);
		settings.setReportingUrl("http://localhost:8080/");
		try {
			Writer writer = new FileWriter(mSettingsFile);
			Gson gson = new GsonBuilder().create();
			gson.toJson(settings, writer);
			writer.close();
		} catch (IOException e) {
			throw new IllegalStateException("Not able to write to file");
		}
	}
}
