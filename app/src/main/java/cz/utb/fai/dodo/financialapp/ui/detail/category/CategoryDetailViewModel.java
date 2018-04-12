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

    public String categoryName;
    public String suma;

    public CategoryDetailViewModel(@NonNull Application application, List<Transaction> transactions) {
        super(application);
        this.transactions = transactions;
    }
}
