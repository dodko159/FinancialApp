package cz.utb.fai.dodo.financialapp.ui.detail.category;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.databinding.CategoryDetailDataBinding;
import cz.utb.fai.dodo.financialapp.common.adapters.AdapterTransaction;
import cz.utb.fai.dodo.financialapp.common.Category;
import cz.utb.fai.dodo.financialapp.common.Transaction;
import cz.utb.fai.dodo.financialapp.ui.detail.transaction.TransactionDetail;

public class CategoryDetail extends AppCompatActivity implements IAdapterItemClicked<Transaction>{

    /***** CONSTANTS *****/
    private static final String TRANSACTIONS = "transactions";
    private static final String TOTALPRICE = "totalprice";
    private static final String CATEGORY = "category";

    /***** VARS *****/
    CategoryDetailDataBinding categoryDetailDataBinding;

    /***** START METHODS *****/
    @NonNull
    public static Intent startIntent(@NonNull Context context) {
        return new Intent(context, CategoryDetail.class);
    }

    /**
     *
     * @param context aktualny context
     * @param transactions tranzkacie na zobrazenie
     * @return Vrací intent s vloženým listom tranyakcii
     */
    public static Intent startIntent(@NonNull Context context, List<Transaction> transactions, double total, int category) {
        Intent intent = startIntent(context);

        intent.putExtra(TRANSACTIONS, transactions.toString()).putExtra(TOTALPRICE, total).putExtra(CATEGORY, category);

        return intent;
    }

    /***** LIFECYCLE METHODS *****/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String transjson = null;

        if (getIntent().getExtras() != null){
            transjson = getIntent().getStringExtra(TRANSACTIONS);
        }

        List<Transaction> transactions;

        if(transjson != null){
            transactions = Transaction.transactionListFromJson(transjson);
            init(transactions);
        }else{
            Toast.makeText(this,R.string.transactions_loading_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /***** HELPER METHODS *****/

    private void init(List<Transaction> transactions) {
        CategoryDetailViewModel viewModel = ViewModelProviders.of(this).get(CategoryDetailViewModel.class);
        viewModel.setTransactions(transactions);

        if (getIntent().getExtras() != null){
            viewModel.setCategoryName(Category.getCategoryName(getIntent().getIntExtra(CATEGORY,0)));
            viewModel.setTotalPrice(getIntent().getDoubleExtra(TOTALPRICE, 0));
        }

        categoryDetailDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_category_detail);
        categoryDetailDataBinding.setVm(viewModel);

        RecyclerView recyclerView = categoryDetailDataBinding.recycleViewCategory;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        AdapterTransaction adapter = new AdapterTransaction(new ArrayList<Transaction>(), this);
        adapter.setNewList(viewModel.getTransactions());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(Transaction transaction) {
        Intent intent = TransactionDetail.startIntent(this, transaction);
        this.startActivity(intent);
    }
}
