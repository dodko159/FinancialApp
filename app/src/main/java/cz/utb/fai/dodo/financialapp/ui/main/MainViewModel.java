package cz.utb.fai.dodo.financialapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.shared.User;
import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

/**
 * Created by Dodo on 30.03.2018.
 */

public class MainViewModel extends AndroidViewModel{

    /**** CONSTANTS ****/
    private boolean INCOMES = true;
    private boolean COSTS = false;

    /***** VARS *****/
    private User user;
    private Context context;
    private String month;
    private Boolean[] uiSetings = {false, false};
    private boolean isActiveIncomes = true;

    private HashMap<Integer, List<Transaction>> groupedMapIncomes = new HashMap<>();
    private HashMap<Integer, Double> pricesIncomes = new HashMap<>();

    private HashMap<Integer, List<Transaction>> groupedMapCosts = new HashMap<>();
    private HashMap<Integer, Double> pricesCosts = new HashMap<>();

    private ValueEventListener listenerForIncomes;
    private ValueEventListener listenerForCosts;

    private DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference(Transaction.INCOMES);
    private DatabaseReference costRef = FirebaseDatabase.getInstance().getReference(Transaction.COSTS);

    private MutableLiveData<HashMap<Integer, Double>> prices = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Float>> percentage = new MutableLiveData<>();
    private HashMap<Integer, List<Transaction>> groupedMap = new HashMap<>();

    private int showTransaction;
    private int showNoTransaction;
    private String transactionType;

    /***** CONSTRUCTOR *****/
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.user = MyShared.getUser(context);

        prices.setValue(null);
        percentage.setValue(null);
    }

    /**** GET, SET****/

    public void setMonth(String month) {
        this.month = month;
    }

    public MutableLiveData<HashMap<Integer, Double>> getPrices() {
        return prices;
    }

    public HashMap<Integer, List<Transaction>> getGroupedMap() {
        return groupedMap;
    }

    public int getShowTransaction() {
        return showTransaction;
    }

    public int getShowNoTransaction() {
        return showNoTransaction;
    }

    public String getTransactionType() {
        return transactionType;
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
                    processData(INCOMES, Transaction.transactionListFromFirebaseJson(dataSnapshot.getValue().toString()));
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
                    processData(COSTS, Transaction.transactionListFromFirebaseJson(dataSnapshot.getValue().toString()));
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
        this.showTransaction = showTransaction ? View.VISIBLE : View.GONE;
        this.showNoTransaction = showTransaction ? View.GONE : View.VISIBLE;
    }

    private void setIncomesOrCost(boolean incomes){
        if(incomes){
            if(uiSetings[0]){
                prices.setValue(pricesIncomes);
                groupedMap = groupedMapIncomes;
            }
            transactionType = context.getResources().getString(R.string.income);
            setUI(uiSetings[0]);
        }else {
            if(uiSetings[1]) {
                prices.setValue(pricesCosts);
                groupedMap = groupedMapCosts;
            }
            transactionType = context.getResources().getString(R.string.cost);
            setUI(uiSetings[1]);
        }
    }

    public void start(){
        setListeners();

        setIncomesOrCost(INCOMES);
    }

    public void switchToIncomes(View v){
        setIncomesOrCost(INCOMES);
        isActiveIncomes = INCOMES;
    }

    public void switchToCosts(View v){
        setIncomesOrCost(COSTS);
        isActiveIncomes = COSTS;
    }


}
