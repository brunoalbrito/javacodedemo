package cn.java.core.helper.csv.apachecsv;

import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CsvHelper {
	public static void testParse() {
		try {
			Reader in = new FileReader("path/to/file.csv");
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
			for (CSVRecord record : records) {
				String lastName = record.get("Last Name");
				String firstName = record.get("First Name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Iterable<CSVRecord> parseCsv(String fileName) {
		try {
			Reader in = new FileReader(fileName);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
			return records;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
