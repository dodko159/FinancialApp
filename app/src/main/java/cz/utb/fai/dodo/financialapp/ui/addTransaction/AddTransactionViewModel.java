package cz.utb.fai.dodo.financialapp.ui.addTransaction;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.shared.DBManager;
import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

/**
 * Created by Dodo on 17.04.2018.
 */

public class AddTransactionViewModel extends AndroidViewModel {

    /**** VARS ****/
    private String priceString;
    private int category;
    private MutableLiveData<Boolean> income = new MutableLiveData<>();
    private MutableLiveData<Boolean> closeActivity = new MutableLiveData<>();
    private String description;
    private String currency;

    private Transaction transaction;

    private Context context;

    /**** CONSTRUCTOR ****/
    public AddTransactionViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();

        priceString = "";
        category = -1;
        income.setValue(true);
        closeActivity.setValue(false);
        description = null;
        currency = Transaction.CURRENCY;
    }

    /**** GET, SET ****/

    public String getPriceString() {
        return priceString;
    }

    public void setPriceString(String priceString) {
        this.priceString = priceString;
    }

    public String getCurrency() {
        return currency;
    }

    public MutableLiveData<Boolean> getIncomeLive(){
        return income;
    }

    public Boolean getIncome() {
        return income.getValue();
    }

    public void setIncome(Boolean income) {
        this.income.setValue(income);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public MutableLiveData<Boolean> getCloseActivity() {
        return closeActivity;
    }

    /**** HELPER METHODS ****/

    /**
     * Ukadá tranzakciu do DB ak sú vyplnené povinné polia (cena, kategoria)
     * @param v
     */
    public void save(View v){

        double price = 0;

        if (priceString != "") {
            price = Double.parseDouble(priceString);
        }

        if(category <0 && price <= 0 ){
            Toast.makeText(context, R.string.set_price_and_category, Toast.LENGTH_SHORT).show();
        }else if(category <0){
            Toast.makeText(context, R.string.select_category, Toast.LENGTH_SHORT).show();
        }else if(price <= 0){
            Toast.makeText(context, R.string.set_price, Toast.LENGTH_SHORT).show();
        }else{
            transaction = new Transaction(System.currentTimeMillis(), category, price, description);

            DBManager.saveTransactionToDB(MyShared.getUser(context).getUid(), transaction, income.getValue());

            Toast.makeText(context, R.string.save_completed, Toast.LENGTH_SHORT).show();
            closeActivity.setValue(true);
        }
    }
}
