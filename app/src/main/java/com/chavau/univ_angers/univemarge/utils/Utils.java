package com.chavau.univ_angers.univemarge.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    // https://developer.android.com/reference/java/text/SimpleDateFormat.html
    private static final String pattern = "yyyy-MM-dd HH:mm:ss.S";

    public static String convertDateToString(Date date) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Date convertStringToDate(String str) {
        DateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // TODO: test
    public static Blob convertByteToBlob(byte[] photo) {
//        try {
//            Blob blob = null; // TODO: check this
//            blob.setBytes(1, photo);
//            return blob;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    // TODO: test
    public static String convertBlobToString(Blob photo) {
//        try {
//            return new String(photo.getBytes(1, (int)photo.length()));
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    public static String IntToDayOfWeek(int number) {
        // chez les anglais la semaine commence à dimanche
        switch (number) {
            case 2 : return "Lundi";
            case 3 : return "Mardi";
            case 4 : return "Mercredi";
            case 5 : return "Jeudi";
            case 6 : return "Vendredi";
            case 7 : return "Samedi";
            case 1 : return "Dimanche";

            default : return "error";
        }
    }

    public static String convertDateToStringHour(Date date) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);
    }

    /**
     * @param activity
     * @return true si connexion internet trouvée
     */
    public static boolean isConnectedInternet(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
        {
            NetworkInfo.State networkState = networkInfo.getState();
            if (networkState.compareTo(NetworkInfo.State.CONNECTED) == 0)
            {
                return true;
            }
            else return false;
        }
        else return false;
    }

}
