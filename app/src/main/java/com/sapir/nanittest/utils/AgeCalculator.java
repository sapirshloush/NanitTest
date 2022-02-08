package com.sapir.nanittest.utils;

import android.annotation.SuppressLint;
import android.util.Log;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgeCalculator {
    private static final String TAG = "AgeCalculator";

    @SuppressLint("SimpleDateFormat")
    public static Period getBabyAge(int birthYear, int birthMonth, int birthDay) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date birthDate = null;
        Date todayDate = null;
        Period monthPeriod = null;
        Period yearsPeriod = null;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        try {
            birthDate = sdf.parse(birthDay + "/" + birthMonth + "/" + birthYear);
            todayDate = sdf.parse(day + "/" + month + "/" + year);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        if (birthDate != null && todayDate != null) {
            long startdate = birthDate.getTime();
            long endDate = todayDate.getTime();
            monthPeriod = new Period(startdate, endDate, PeriodType.months());
            yearsPeriod = new Period(startdate, endDate, PeriodType.years());
        }

        if (monthPeriod != null && monthPeriod.getMonths() > 12 && yearsPeriod.getYears() > 0) {
            return yearsPeriod;
        } else {
            return monthPeriod;
        }

    }
}
