package cz.utb.fai.dodo.financialapp.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;
import java.util.Map;

import cz.utb.fai.dodo.financialapp.shared.Category;
import cz.utb.fai.dodo.financialapp.shared.MyDate;
import cz.utb.fai.dodo.financialapp.shared.User;
import cz.utb.fai.dodo.financialapp.shared.DBManager;
import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.shared.Transaction;
import cz.utb.fai.dodo.financialapp.ui.BaseViewModel;

/**
 * Created by Dodo on 30.03.2018.
 */

public class MainViewModel extends BaseViewModel{

    private static Boolean INCOMES = true;
    private static Boolean COSTS = false;

    private List<Transaction> transactions;
    private User user;
    private Context context;
    private Map<String, List<Transaction>> transactionMap;

    public int showTransaction;
    public int showNoTransaction;

    public MainViewModel(@NonNull Context context) {
        this.context = context;
        this.user = MyShared.getUser(context);

        //testDataSave();

        init();
    }

    private void testDataSave() {
        DBManager.saveDataToDB(user.getUid(), new Transaction(System.currentTimeMillis(), Category.WORK, 1260, "popis"),INCOMES);
        DBManager.saveDataToDB(user.getUid(), new Transaction(System.currentTimeMillis(), Category.CHILDREN, 400, "popis 2"),COSTS);
    }

    private void init() {
        //MyShared.clerSharedByKey(context, Transaction.INCOMES);
        loadData();
    }

    private void loadData(){

        //// TODO: 01.04.2018 akualne nacitany mesiac, ... , vyber incomes alebo costs
        Long time = System.currentTimeMillis();
        String selectMonth = MyDate.longTimeToMonthYear(time);

        Boolean incomes = true;
        String incomesStr = Transaction.INCOMES;
        if (!incomes) incomesStr = Transaction.COSTS;


        DBManager.loadTransactionsFromDB(context, user.getUid(), incomes);
        transactionMap = MyShared.loadTransactions(context, incomesStr);

        addThisMonthIfNotExist();

        Boolean isTranInDB = Transaction.areTransactionsEmpty(transactionMap, selectMonth);
        setUI(isTranInDB);
    }

    private void addThisMonthIfNotExist() {
        String thisMonth = MyDate.longTimeToMonthYear(System.currentTimeMillis());
        Boolean contains = transactionMap.containsKey(thisMonth);

        if (!contains){
            transactionMap.put(thisMonth, null);
        }
    }

    private void setUI(Boolean showTransaction) {
        this.showTransaction = showTransaction ? View.VISIBLE : View.GONE;
        this.showNoTransaction = showTransaction ? View.GONE : View.VISIBLE;
    }
}
