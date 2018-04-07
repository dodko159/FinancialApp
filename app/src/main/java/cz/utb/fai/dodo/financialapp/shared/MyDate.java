package cz.utb.fai.dodo.financialapp.shared;

import android.support.annotation.NonNull;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Dodo on 04.04.2018.
 */

public class MyDate {

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
}
