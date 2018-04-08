package cz.utb.fai.dodo.financialapp.ui.detail.category;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.databinding.CategoryDetailDataBinding;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

public class CategoryDetail extends AppCompatActivity {

    /***** CONSTANTS *****/
    private static final String TRANSACTIONS = "transactions";

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
    public static Intent startIntent(@NonNull Context context, List<Transaction> transactions) {
        Intent intent = startIntent(context);
        //todo: skontrolovat toString
        intent.putExtra(TRANSACTIONS, transactions.toString());

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String transjson = null;

        if (getIntent().getExtras() != null){
            transjson = getIntent().getStringExtra(TRANSACTIONS);
        }

        List<Transaction> transactions = null;

        if(transjson != null){
            //todo: skonvertovat transjson na list
        }else{
            //todo nieco
            Toast.makeText(this,R.string.transaction_loading_error, Toast.LENGTH_SHORT).show();
            finish();
        }

        categoryDetailDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_detail);
        categoryDetailDataBinding.setVm(new CategoryDetailViewModel(CategoryDetail.this, transactions));

    }
}
