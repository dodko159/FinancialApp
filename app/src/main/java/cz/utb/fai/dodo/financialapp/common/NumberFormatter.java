package cz.utb.fai.dodo.financialapp.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Dodo on 27.04.2018.
 */

public class NumberFormatter {

    private static DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.UK);
    private static DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

    public static String formateNumber(double number, Character separator){

        symbols.setGroupingSeparator(separator);
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }
}
