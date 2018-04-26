package cz.utb.fai.dodo.financialapp.shared;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dodo on 04.04.2018.
 */

public class MyDate {

    private static final String dateFormat = "dd.MM.yyyy HH:mm";

    /***
     *
     * @param month cislo mesiaca
     * @return Vrati nazov mesiaca
     */
    public static String getMonthName(int month){
        return new DateFormatSymbols().getMonths()[month - 1];
    }

    /***
     *
     * @param month cislo mesiaca
     * @param locale region
     * @return Vrati nazov mesiaca
     */
    public static String getMonthName(int month, Locale locale){
        new DateFormatSymbols();
        return DateFormatSymbols.getInstance(locale).getMonths()[month - 1];
    }

    /***
     * Skonvertuje cas vo formate long na string YEAR_MONTH
     * @param time cas vo formate long
     * @return Vrati string datumu vo formate YEAR_MONTH
     */
    @NonNull
    public static String longTimeToMonthYear(Long time){

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);

        return c.get(Calendar.YEAR)+"_"+(c.get(Calendar.MONTH) + 1);
    }

    /***
     * Konvertuje datum vo formate Long na datum a cas
     * @param time datum vo formate Long
     * @return datum vo formate - "yyyy MM dd HH:mm:ss"
     */
    public static String longTimeToDate(Long time){
        Date date = new Date(time);
        //Format format = DateFormat.getDateTimeInstance().getNumberFormat();
        Format format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    /***
     * Konvertuje datum y formatu Year_Month (2018_3) do Motnh Year (March 2018)
     * @param key kluc z Firebase vo formate YYYY_M
     * @return string s nazvom mesiaca a rokom - Month YYYY
     */
    @NonNull
    public static String toPreatyMonthYear(@NonNull String key) {

            String[] ym = key.split("_");
            String month = getMonthName(Integer.parseInt(ym[1]));

        return month + " " + ym[0];
    }
}
