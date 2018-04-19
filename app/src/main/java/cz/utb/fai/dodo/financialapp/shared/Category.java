package cz.utb.fai.dodo.financialapp.shared;


import cz.utb.fai.dodo.financialapp.R;

/**
 * Created by Dodo on 30.03.2018.
 */

public final class Category {
    public static final int OFFSET = 10;

    public static final int WORK = 1, LOAN = 2, WIN = 3, OTHERINCOME = 0;
    public static final int FOOD = OFFSET+1, CLOTHES = OFFSET+2, TRAVELING = OFFSET+3, CAR = OFFSET+4, CHILDREN = OFFSET+5,
            HOLIDAY = OFFSET+6, HOME = OFFSET+7, ENTERTAINMENT = OFFSET+8, SERVICES = OFFSET+9,
            ANIMALS = OFFSET+10, BILLS = OFFSET+11, ELECTRONICS = OFFSET+12, OTHERCOST = OFFSET;

    //todo dat do resource
    private static String[] Income = {
            "Other", "Work", "Loan", "Win"
    };

    private static String[] Cost ={
        "Other", "Food", "Clothes", "Traveling", "Car", "Children",
        "Holiday", "Home", "Entertainment", "Service",
        "Animals", "Bills", "Electronics"
    };

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
