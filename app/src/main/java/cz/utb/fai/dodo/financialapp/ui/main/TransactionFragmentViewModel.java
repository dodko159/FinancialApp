package cz.utb.fai.dodo.financialapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.DBManager;
import cz.utb.fai.dodo.financialapp.common.MyShared;
import cz.utb.fai.dodo.financialapp.common.NumberFormatter;
import cz.utb.fai.dodo.financialapp.common.Transaction;
import cz.utb.fai.dodo.financialapp.common.User;

/**
 * Created by Dodo on 24.04.2018.
 */

public class TransactionFragmentViewModel extends AndroidViewModel{

    /**** CONSTANTS ****/
    private boolean INCOMES = true;
    private boolean COSTS = false;

    /***** VARS *****/
    private User user;
    private String month;
    private Boolean[] uiSetings = {false, false};
    private boolean isActiveIncomes = true;

    private HashMap<Integer, List<Transaction>> groupedMapIncomes = new HashMap<>();
    private HashMap<Integer, Double> pricesIncomes = new HashMap<>();

    private HashMap<Integer, List<Transaction>> groupedMapCosts = new HashMap<>();
    private HashMap<Integer, Double> pricesCosts = new HashMap<>();

    private ValueEventListener listenerForIncomes;
    private ValueEventListener listenerForCosts;

    private DatabaseReference incomeRef = DBManager.incomeRef;
    private DatabaseReference costRef = DBManager.costRef;

    private MutableLiveData<HashMap<Integer, Double>> prices = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Float>> percentage = new MutableLiveData<>();
    private HashMap<Integer, List<Transaction>> groupedMap = new HashMap<>();

    public ObservableField<Integer> showTransaction = new ObservableField<>();
    public ObservableField<Integer> showNoTransaction = new ObservableField<>();
    public ObservableField<String> transactionType = new ObservableField<>("");
    public ObservableField<String> transactionSuma = new ObservableField<>();

    /***** CONSTRUCTOR *****/
    public TransactionFragmentViewModel(@NonNull Application application) {
        super(application);
        this.user = MyShared.getUser(this.getApplication().getApplicationContext());

        prices.setValue(null);
        percentage.setValue(null);
    }

    /**** GET, SET****/

    void setMonth(String month) {
        this.month = month;
    }

    MutableLiveData<HashMap<Integer, Double>> getPrices() {
        return prices;
    }

    HashMap<Integer, List<Transaction>> getGroupedMap() {
        return groupedMap;
    }

    /**** LIFECYCLE METHODS ****/

    @Override
    protected void onCleared() {
        super.onCleared();

        incomeRef.removeEventListener(listenerForIncomes);
        costRef.removeEventListener(listenerForCosts);
    }

    /**** HELPER METHODS ****/

    private void setListeners() {
        listenerForIncomes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    processData(INCOMES, Transaction.transactionListFromFirebase(dataSnapshot));
                    setIncomesOrCost(isActiveIncomes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
            }
        };

        listenerForCosts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    processData(COSTS, Transaction.transactionListFromFirebase(dataSnapshot));
                    setIncomesOrCost(isActiveIncomes);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
            }
        };

        incomeRef = incomeRef.child(user.getUid()).child(month);
        costRef = costRef.child(user.getUid()).child(month);

        incomeRef.addValueEventListener(listenerForIncomes);
        costRef.addValueEventListener(listenerForCosts);
    }

    private void processData(boolean incomes, List<Transaction> transactions){

        Boolean isNotEmpty = false;
        if (transactions != null) {
            isNotEmpty = !transactions.isEmpty();
        }

        if(incomes){
            uiSetings[0] = isNotEmpty;
        }else {
            uiSetings[1] = isNotEmpty;
        }

        if(isNotEmpty){
            groupTransactionByCategoryAndSumPrices(transactions, incomes);
        }
    }

    private void groupTransactionByCategoryAndSumPrices(List<Transaction> transactionList, boolean incomes) {

        HashMap<Integer, List<Transaction>> groupedMap = new HashMap<>();
        HashMap<Integer, Double> prices = new HashMap<>();

        for (Transaction t : transactionList) {
            Integer key = t.getCategory();
            if (!groupedMap.containsKey(key)) {
                List<Transaction> list = new ArrayList<>();
                list.add(t);

                groupedMap.put(key, list);
                prices.put(key,t.getPrice());
            } else {
                groupedMap.get(key).add(t);

                Double sum = prices.get(key);
                sum += t.getPrice();
                prices.put(key, sum);
            }
        }

        if(incomes){
            groupedMapIncomes = groupedMap;
            pricesIncomes = prices;
        }else {
            groupedMapCosts = groupedMap;
            pricesCosts = prices;
        }
    }

    private void addThisMonthIfNotExist() {
        /*String thisMonth = MyDate.longTimeToMonthYear(System.currentTimeMillis());
        Boolean contains = transactionMap.containsKey(thisMonth);

        if (!contains){
            transactionMap.put(thisMonth, null);
        }*/
    }

    private void setUI(Boolean showTransaction) {
        int show, noShow;
        show = showTransaction ? View.VISIBLE : View.GONE;
        noShow = showTransaction ? View.GONE : View.VISIBLE;

        this.showTransaction.set(show);
        this.showNoTransaction.set(noShow);
    }

    void setIncomesOrCost(boolean incomes){
        isActiveIncomes = incomes;

        if(incomes){
            if(uiSetings[0]){
                prices.setValue(pricesIncomes);
                groupedMap = groupedMapIncomes;
                recalcSum();
            }
            transactionType.set(getApplication().getResources().getString(R.string.income));
            setUI(uiSetings[0]);
        }else {
            if(uiSetings[1]) {
                prices.setValue(pricesCosts);
                groupedMap = groupedMapCosts;
                recalcSum();
            }
            transactionType.set(getApplication().getResources().getString(R.string.cost));
            setUI(uiSetings[1]);
        }
    }

    public void init(){
        setListeners();

        setIncomesOrCost(INCOMES);
    }

    private void recalcSum(){
        double sum = 0;

        for(Map.Entry<Integer, Double> entry : prices.getValue().entrySet()) {
            sum += entry.getValue();
        }

        transactionSuma.set(NumberFormatter.formateNumber(sum, ' ') + " " + Transaction.CURRENCY);
    }
}
