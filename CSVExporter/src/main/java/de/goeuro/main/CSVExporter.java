package de.goeuro.main;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.JsonArray;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.goeuro.reader.GEJSonReader;
import de.goeuro.writer.CSVFileWriter;

public class CSVExporter {
	private static final Logger LOG = LoggerFactory.getLogger(CSVExporter.class);

	public static final  String jsonUri = "http://api.goeuro.com/api/v2/position/suggest/en/";
	
	
	public static void main(String[] args) {
		
		if(args.length>0){
			String cityName = args[0];
			LOG.info("Starting to convert json document for city: " + cityName);
			
				try {
					URL url = new URL(jsonUri+cityName);
					GEJSonReader reader = new GEJSonReader();
					CSVFileWriter cvsFileWriter = new CSVFileWriter();
					
					JsonArray  jsoncities = reader.getGoEuroCities(url); 
					
					cvsFileWriter.exportJsonToCSV(jsoncities, cityName);
				
				} catch (MalformedURLException e) {
					LOG.error("Error: problem to solve the the url : {}" , jsonUri+ cityName);
					System.err.println("Error: problem to solve the the url : " + jsonUri+ cityName);
					e.printStackTrace();
				} catch (IOException e) {
					LOG.error("Error: Can not write file");
					System.err.println("Error: Can not write file");
					e.printStackTrace();
				} 
			
		}else{
			LOG.error("Error: Expected city name as argument");
			System.err.println("Error: Expected city name as argument");
		}

	}

}
