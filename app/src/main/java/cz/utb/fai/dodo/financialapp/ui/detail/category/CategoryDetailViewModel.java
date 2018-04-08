package cz.utb.fai.dodo.financialapp.ui.detail.category;

import android.content.Context;
import android.support.annotation.NonNull;
import java.util.List;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

/**
 * Created by Dodo on 08.04.2018.
 */

public class CategoryDetailViewModel {

    private Context context;
    private List<Transaction> transactions;

    public String categoryName;
    public String suma;

    public CategoryDetailViewModel(@NonNull Context context, List<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }
}
