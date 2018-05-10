package cz.utb.fai.dodo.financialapp.ui.detail.category;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import java.util.List;

import cz.utb.fai.dodo.financialapp.common.NumberFormatter;
import cz.utb.fai.dodo.financialapp.common.Transaction;

/**
 * Created by Dodo on 08.04.2018.
 */

public class CategoryDetailViewModel extends AndroidViewModel{

    private List<Transaction> transactions;
    private double totalPrice;

    public String categoryName;

    public String suma;

    CategoryDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        suma = NumberFormatter.formateNumber(totalPrice, ' ') + " " + Transaction.CURRENCY;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
