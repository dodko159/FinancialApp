package cz.utb.fai.dodo.financialapp.ui.detail.transaction;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.databinding.TransactionDetailDataBinding;
import cz.utb.fai.dodo.financialapp.shared.DBManager;
import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

public class TransactionDetail extends AppCompatActivity {

    /***** CONSTANTS *****/
    private static final String TRANSACTION = "transaction";

    /***** VARS *****/
    TransactionDetailDataBinding transactionDetailDataBinding;
    View.OnClickListener deleteClickListener;
    FloatingActionButton deleteButton;

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

            init(transaction);
        }else{
            Toast.makeText(this,R.string.transaction_loading_error, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        deleteButton.setOnClickListener(deleteClickListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        deleteButton.setOnClickListener(null);
    }

    /**** HELPER METHODS *****/

    private void init(Transaction transaction) {

        TransactionDetailViewModel viewModel = ViewModelProviders.of(this).get(TransactionDetailViewModel.class);
        viewModel.setTransaction(transaction);

        transactionDetailDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_detail);
        transactionDetailDataBinding.setVm(viewModel);

        deleteButton = transactionDetailDataBinding.deleteButton;

        final Transaction trans = transaction;
        deleteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Context context = TransactionDetail.this;

                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setTitle(R.string.want_delete_title);
                dialog.setMessage(context.getResources().getString(R.string.want_delete_text));
                dialog.setCancelable(true);

                dialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.YES),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBManager.removeTransaction(MyShared.getUser(context).getUid(),trans);
                                Toast.makeText(context, R.string.TRANSACTION_WILL_BE_DELETE, Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            }
                        });
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getResources().getString(R.string.NO),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                dialog.show();

            }
        };
    }
}
