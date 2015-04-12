package de.goeuro.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.Date;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVFileWriter {
	 private static final String DD_MM_YYYY = "ddMMyyyy";

	 private static final Logger LOG = LoggerFactory.getLogger(CSVFileWriter.class);
	
	private static final String BLANK = " ";
	private static final String COMMA = ",";
	private static final String LONGITUDE = "longitude";
	private static final String LATITUDE = "latitude";
	private static final String GEO_POSITION = "geo_position";
	private static final String TYPE = "type";
	private static final String NAME2 = "name";
	private static final String _ID = "_id";
	private static final String DOUBLE_QUOTE = "\"";
	private static final String DD_MM_YYYY_HH_MM_SS = "ddMMyyyyHHmmss";
	private static final String UNDER_SCORE = "_";
	private static final String ISO_8859_1 = "ISO-8859-1";
	
	private static final String CSV_EXTENSION = ".csv";
	
	/**
	 * Create a CSV file from JsonArray
	 * 
	 * @param jsonArray read from the API
	 * @param cityName used for result file
	 * @throws IOException
	 */
	public void exportJsonToCSV(JsonArray jsonArray, String cityName) throws IOException{
		LOG.info("Creating CSV file");
		
		File outputFile = this.createCVSFile(cityName);
		
		FileOutputStream fileOutPutStream = null;
		OutputStreamWriter outputStreamWriter = null;
		BufferedWriter fileBufferedWriter = null;
		try {
			fileOutPutStream = new FileOutputStream(outputFile);
			outputStreamWriter = new OutputStreamWriter(fileOutPutStream, ISO_8859_1);
			fileBufferedWriter = new BufferedWriter(outputStreamWriter);
			appendJsonArray(fileBufferedWriter, jsonArray);
			
		}finally {
			if (fileBufferedWriter != null) {
				fileBufferedWriter.flush();
				fileBufferedWriter.close();
			}
		}
		LOG.info("Created CSV file: {}", outputFile.getAbsolutePath() );
	}
	
	
	
	private void appendJsonArray(BufferedWriter fileBufferedWriter,
			JsonArray jsonArray) throws IOException {
		LOG.info("Appending data");
		String firstLine = "_id,name,type,latitude,longitude";
		newLine(fileBufferedWriter, firstLine, false);
		
		for(JsonObject result : jsonArray.getValuesAs(JsonObject.class)){
			String resultLine = StringUtils.EMPTY;
			resultLine = getJsonLine(result);
			newLine(fileBufferedWriter, resultLine, false);
			LOG.debug("Wrote line: {}", resultLine);
		}
		LOG.info("Appended {} lines", jsonArray.size());
		
	}
	
	/**
	 * Convert JSon object to CSV object 
	 * @param result
	 * @return
	 */
	private String getJsonLine(JsonObject result) {
		String resultLine = StringUtils.EMPTY;
		resultLine += doubleQuoteString(result.getJsonNumber(_ID).toString()) + COMMA;
		resultLine += doubleQuoteString(result.getString(NAME2)) + COMMA;
		resultLine += doubleQuoteString(result.getString(TYPE)) + COMMA;
		resultLine += doubleQuoteString(result.getJsonObject(GEO_POSITION).getJsonNumber(LATITUDE).toString()) + COMMA;
		resultLine += doubleQuoteString(result.getJsonObject(GEO_POSITION).getJsonNumber(LONGITUDE).toString());
		return resultLine;
	}

	
	
	protected void newLine(BufferedWriter bufferedWriter, String line, Boolean onlyAppend) throws IOException {
		if ((line != null) && (line.length() > 0)) {
			append(bufferedWriter, line);
			if (!onlyAppend) {
				bufferedWriter.newLine();
			}
		}
	}
	
	private void append(BufferedWriter bufferedWriter, String line) throws IOException {
		bufferedWriter.write(line);
	}
	
	private String doubleQuoteString(String value){
		return (StringUtils.contains(value,COMMA)||
				StringUtils.startsWith(value,BLANK)||
				StringUtils.endsWith(value,BLANK)||
				StringUtils.contains(value, DOUBLE_QUOTE))? 
				DOUBLE_QUOTE +value + DOUBLE_QUOTE :value;
		
	}

	private File createCVSFile(String cityName){
		
		Date date = new Date();
		String ddmmyyyyhhmmss = DateFormatUtils.format(date,  DD_MM_YYYY_HH_MM_SS);
		String outputFolder = Paths.get(FileUtils.getUserDirectoryPath(), DateFormatUtils.format(date, DD_MM_YYYY )).toString();
		String fileName = cityName + UNDER_SCORE + ddmmyyyyhhmmss + CSV_EXTENSION; 
		
		//create output directory
		File outputDirectory = new File(outputFolder);
		if(!outputDirectory.exists()){
			outputDirectory.mkdir();
		}
		
		
		File outputFile = new File(outputFolder+ "\\" +fileName);
		
		
		return outputFile;
		
	}
}
