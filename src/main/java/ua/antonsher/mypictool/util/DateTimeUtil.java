package ua.antonsher.mypictool.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

	public static final String DATE_FORMAT = "YYYY-MM-dd HH:mm:ss";

	public static String currentTimeCaption() {
	    return new SimpleDateFormat(DATE_FORMAT).format(new Date());
	}

}
