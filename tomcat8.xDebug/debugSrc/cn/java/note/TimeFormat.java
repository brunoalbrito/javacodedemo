package cn.java.note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFormat {

	public static void main(String[] args) {
		SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timestampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(timestampFormat.format(new Date()));;
	}

}
