package cz.utb.fai.dodo.financialapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.utb.fai.dodo.financialapp.shared.Category;
import cz.utb.fai.dodo.financialapp.shared.MyDate;
import cz.utb.fai.dodo.financialapp.shared.User;
import cz.utb.fai.dodo.financialapp.shared.DBManager;
import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.shared.Transaction;

/**
 * Created by Dodo on 30.03.2018.
 */

public class MainViewModel extends AndroidViewModel{

    /**** CONSTANTS ****/
    private static Boolean INCOMES = true;
    private static Boolean COSTS = false;

    /***** VARS *****/
    private User user;
    private List<Transaction> transactionList = new ArrayList<>();
    private Context context;
    private HashMap<Integer, List<Transaction>> groupedMap = new HashMap<>();
    private HashMap<Integer, Double> prices = new HashMap<>();

    public int showTransaction;
    public int showNoTransaction;

    /***** CONSTRUCTOR *****/
    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = this.getApplication();
        this.user = MyShared.getUser(context);

        //testDataSave();

        init();
    }

    /**** GETS ****/

    public HashMap<Integer, Double> getPrices() {
        return prices;
    }

    public HashMap<Integer, List<Transaction>> getGroupedMap() {
        return groupedMap;
    }

    /**** HELPER METHODS ****/
    private void testDataSave() {
        DBManager.saveTransactionToDB(user.getUid(), new Transaction(System.currentTimeMillis(), Category.WORK, 1260, "popis"),INCOMES);
        DBManager.saveTransactionToDB(user.getUid(), new Transaction(System.currentTimeMillis(), Category.CHILDREN, 400, "popis 2"),COSTS);
    }

    private void init() {
        //MyShared.clearSharedByKey(context, Transaction.INCOMES);
        loadData();
    }

    private void loadData(){

        //// TODO: 01.04.2018 akualne nacitany mesiac, ... , vyber incomes alebo costs
        Long time = System.currentTimeMillis();
        String selectedMonth = MyDate.longTimeToMonthYear(time);

        Boolean incomes = true;
        String incomesStr = Transaction.INCOMES;
        if (!incomes) incomesStr = Transaction.COSTS;


        DBManager.loadTransactionsFromDB(context, user.getUid(), selectedMonth, incomes);
        transactionList = MyShared.loadTransactions(context, incomesStr);

        Boolean isTranInDB = Transaction.areTransactionsEmpty(transactionList, selectedMonth);

        if(!isTranInDB){
            groupTransactionByCategoryAndSumPrices();
        }
        setUI(isTranInDB);
    }

    private void groupTransactionByCategoryAndSumPrices() {

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
    }

    private void addThisMonthIfNotExist() {
        /*String thisMonth = MyDate.longTimeToMonthYear(System.currentTimeMillis());
        Boolean contains = transactionMap.containsKey(thisMonth);

        if (!contains){
            transactionMap.put(thisMonth, null);
        }*/
    }

    private void setUI(Boolean showTransaction) {
        this.showTransaction = showTransaction ? View.GONE : View.VISIBLE;
        this.showNoTransaction = showTransaction ? View.VISIBLE : View.GONE;
    }
}
