package cn.java.core.helper.csv.javacsv;

import java.nio.charset.Charset;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
/**
 * http://sourceforge.net/projects/javacsv/files/
 * @author Administrator
 *
 */
public class CsvHelper {

	public static void readCsv(String fileName) {
		try {
			//生成CsvReader对象，以，为分隔符，GBK编码方式
			CsvReader r = new CsvReader(fileName, ',', Charset.forName("UTF-8"));
			//读取表头
			r.readHeaders();
			//逐条读取记录，直至读完
			while (r.readRecord()) {
				//读取一条记录
				System.out.println(r.getRawRecord());
				//按列名读取这条记录的值
				System.out.println(r.get("Name"));
				System.out.println(r.get("class"));
				System.out.println(r.get("number"));
				System.out.println(r.get("sex"));
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeCsv(String fileName) {
		try {
			CsvWriter wr = new CsvWriter(fileName, ',', Charset.forName("UTF-8"));
			String[] contents = { "Lily", "五一", "90", "女" };
			wr.writeRecord(contents);
			wr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
