package de.goeuro.reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class GEJSonReader {
	private static final Logger LOG = LoggerFactory.getLogger(GEJSonReader.class);

	
	public JsonArray getGoEuroCities(URL url){
		try (InputStream jsonInputStream = url.openStream()){
			LOG.info("opening url: " + url.toString());
			
			JsonReader jsonReader = Json.createReader(jsonInputStream);
			
			JsonArray readArray = jsonReader.readArray();
			
			LOG.info("Got Json Array ");

			return readArray;
		
		
		}catch (IOException e) {
			LOG.error(e.getMessage(),e);
			System.err.println("Error: problem to open the input stream");
			e.printStackTrace();
		}
		return null;
	}

	
	
	
}
