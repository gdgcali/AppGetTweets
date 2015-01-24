package co.com.gdgcali.co.com.gdgcali.utils;

/**
 * Created by JuanGuillermo on 24/01/2015.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import android.util.Log;

public class DateUtils {

    public static String setDateFormat(String date){
        String str = removeTimeZone(date);

        String strData = null;
        TimeZone tzUTC = TimeZone.getTimeZone("UTC");
        SimpleDateFormat formatoEntrada = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US);
        formatoEntrada.setTimeZone(tzUTC);
        SimpleDateFormat formatoSalida = new SimpleDateFormat("EEE, dd/MM/yy, HH:mm");

        try {
            strData = formatoSalida.format(formatoEntrada.parse(str));
        } catch (ParseException e) {
            Log.e("Error parser data", Log.getStackTraceString(e));
        }
        return strData;
    }

    public static String removeTimeZone(String data){
        // Elimina " (+ ou -)dddd" Ex: " +3580"
        return data.replaceFirst("(\\s[+|-]\\d{4})", "");
    }
}
