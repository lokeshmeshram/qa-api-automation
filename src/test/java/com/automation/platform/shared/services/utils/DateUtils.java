package com.automation.platform.shared.services.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    //private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar c = Calendar.getInstance();

    public static Date dateValue(int dayToAdd){

        Date dt = new Date();
        c.setTime(dt);
        c.add(Calendar.DATE, dayToAdd);
        dt = c.getTime();
        //System.out.println("----"+dateFormat.format(dt));
        return dt;
    }

}
