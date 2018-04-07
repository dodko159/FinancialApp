package cz.utb.fai.dodo.financialapp.ui.detail.transaction;

import android.content.Context;
import android.support.annotation.NonNull;

import cz.utb.fai.dodo.financialapp.shared.Category;
import cz.utb.fai.dodo.financialapp.shared.MyDate;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

/**
 * Created by Dodo on 07.04.2018.
 */

public class TransactionDetailViewModel {
    private Context context;
    private Transaction transaction;

    public String categoryName;
    public double price;
    public String date;
    public String description;

    public TransactionDetailViewModel(@NonNull Context context, Transaction transaction) {
        this.context = context;
        this.transaction = transaction;

        this.categoryName = Category.getCategoryName(transaction.getCategory());
        this.price = transaction.getPrice();
        this.date = MyDate.longTimeToDate(transaction.getTransactionDate());
        this.description = transaction.getDescription();
    }


}
