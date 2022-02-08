package com.sapir.nanittest.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utilities {

    public static void isStoragePermissionGranted(Activity activity) {
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            }
            if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity,
                        listPermissionsNeeded.toArray(new String[0]), 1);
            } else {
                listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                activity.onRequestPermissionsResult(1,
                        listPermissionsNeeded.toArray(new String[0]), null);
            }
        }
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static long limitTimePicker() {
        Calendar cStart = Calendar.getInstance();
        cStart.set(2010, 1, 1);
        return cStart.getTimeInMillis();
    }
}
