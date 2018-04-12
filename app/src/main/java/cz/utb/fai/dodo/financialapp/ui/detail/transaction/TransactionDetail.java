package cz.utb.fai.dodo.financialapp.ui.detail.transaction;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.databinding.TransactionDetailDataBinding;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

public class TransactionDetail extends AppCompatActivity {

    /***** CONSTANTS *****/
    private static final String TRANSACTION = "transaction";

    /***** VARS *****/
    TransactionDetailDataBinding transactionDetailDataBinding;

    /***** START METHODS *****/
    @NonNull
    public static Intent startIntent(@NonNull Context context) {
        return new Intent(context, TransactionDetail.class);
    }

    /**
     *
     * @param context aktualny context
     * @param transaction tranzkacia na zobrazenie
     * @return Vrací intent s vloženým objektem transaction.
     */
    public static Intent startIntent(@NonNull Context context, Transaction transaction) {
        Intent intent = startIntent(context);
        intent.putExtra(TRANSACTION, transaction.toString());

        return intent;
    }

    /**** LIFECYCLE METHODS ****/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String transjson = null;

        if (getIntent().getExtras() != null){
            transjson = getIntent().getStringExtra(TRANSACTION);
        }

        Transaction transaction = null;

        if(transjson != null){
            transaction = Transaction.fromString(transjson);
        }else{
            //todo nieco
            Toast.makeText(this,R.string.transaction_loading_error, Toast.LENGTH_SHORT).show();
            finish();
        }

        transactionDetailDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_detail);
        transactionDetailDataBinding.setVm(new TransactionDetailViewModel(getApplication(), transaction));

        init();
    }

    /**** HELPER METHODS ****/
    private void init() {

    }
}
