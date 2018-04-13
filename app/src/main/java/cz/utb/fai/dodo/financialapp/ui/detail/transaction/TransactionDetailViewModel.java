package cz.utb.fai.dodo.financialapp.ui.detail.transaction;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import cz.utb.fai.dodo.financialapp.shared.Category;
import cz.utb.fai.dodo.financialapp.shared.MyDate;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

/**
 * Created by Dodo on 07.04.2018.
 */

public class TransactionDetailViewModel extends AndroidViewModel{

    private Transaction transaction;

    public String categoryName;
    public String price;
    public String date;
    public String description;

    public TransactionDetailViewModel(@NonNull Application application, Transaction transaction) {
        super(application);
        this.transaction = transaction;

        this.categoryName = Category.getCategoryName(transaction.getCategory());
        this.price = String.valueOf(transaction.getPrice()) + " " + Transaction.CURRENCY;
        this.date = MyDate.longTimeToDate(transaction.getTransactionDate());
        this.description = transaction.getDescription();
    }


}
