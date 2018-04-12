package cz.utb.fai.dodo.financialapp.shared;

/**
 * Created by Dodo on 10.04.2018.
 */

public class TransactionListItem {
    public String date;
    public String price;

    public TransactionListItem(Long date, double price){
        this.date = MyDate.longTimeToDate(date);
        this.price = String.valueOf(price) + " " + Transaction.CURRENCY;
    }

    public TransactionListItem(String date, String price){
        this.date = date;
        this.price = price;
    }
}
