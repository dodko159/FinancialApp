package cz.utb.fai.dodo.financialapp.ui.detail.category;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import java.util.List;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

/**
 * Created by Dodo on 08.04.2018.
 */

public class CategoryDetailViewModel extends AndroidViewModel{

    private List<Transaction> transactions;
    private double totalPrice;

    public String categoryName;

    public String suma;

    CategoryDetailViewModel(@NonNull Application application, List<Transaction> transactions) {
        super(application);
        this.transactions = transactions;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        suma = totalPrice + " " + Transaction.CURRENCY;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
