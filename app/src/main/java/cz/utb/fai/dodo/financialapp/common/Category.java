package cz.utb.fai.dodo.financialapp.common;

import android.app.Application;
import android.support.annotation.NonNull;

import cz.utb.fai.dodo.financialapp.R;

/**
 * Created by Dodo on 30.03.2018.
 */

public final class Category {
    public static final int OFFSET = 10;

    private static String[] Income;
    private static String[] Cost;

    public static void loadDataFromResources(@NonNull Application application){
        Income = application.getResources().getStringArray(R.array.incomes_array);
        Cost = application.getResources().getStringArray(R.array.costs_array);
    }

    public static String[] getIncome() {
        return Income;
    }

    public static String[] getCost() {
        return Cost;
    }

    public static String getCategoryName(int category){
        if(category < OFFSET){
            return Income[category];
        }else{
            return Cost[category-OFFSET];
        }
    }
}
