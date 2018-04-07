package cz.utb.fai.dodo.financialapp.shared;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Dodo on 30.03.2018.
 */

public class Transaction {

    public static final String INCOMES = "incomes";
    public static final String COSTS = "costs";
    public static final String CURRENCY = "CZK";

    private String uid;
    private long transactionDate;
    private long creationDate;
    private int category;
    private double price;
    private String description;

    /****  CONSTRUCTOR ****/

    public Transaction(long transactionDate, int category, double price, String description) {
        this.transactionDate = transactionDate;
        this.category = category;
        this.price = price;
        this.creationDate = System.currentTimeMillis();
        this.description = description;
    }

    /**** GET, SET ****/

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(long transactionDate) {
        this.transactionDate = transactionDate;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

/**** HELPER METHODS ****/

    /***
     * Prelozi data z Jsonu
     * @param transJson tranzakcie vo formate Json (String)
     * @return vrati data v podobe mapy < kluc, List< Transaction >>
     */
    @Nullable
    public static Map<String, List<Transaction>> transactionMapFromJson(String transJson) {
        Map<String, LinkedTreeMap> myMap;
        Map<String, List<Transaction>> mapList = new HashMap<>();

        myMap = new Gson().fromJson(transJson, Map.class);

        for(Map.Entry<String, LinkedTreeMap> entry : myMap.entrySet()){
            LinkedTreeMap month = entry.getValue();
            List transactions = new ArrayList<>(month.values());

            Type fooType = new TypeToken<List<Transaction>>() {}.getType();
            List<Transaction> transList = new Gson().fromJson(transactions.toString(),fooType);

            mapList.put(entry.getKey(),transList);
        }

        return mapList;
    }

    /***
     * Skontroluje ci su v danom mesiaci nejake tranzakcie
     * @param map tranzakcie vo formate mapy
     * @param month mesiac
     * @return true ak su pre dany mesiac nejake data k dispozicii
     */
    @NonNull
    public static Boolean areTransactionsEmpty(Map map, String month){

        return map.get(month) != null;
    }

    /***
     * Overide metody toString .. konvertuje Transaction na JSon
     * @return tranyakciu vo formate Json
     */
    @Override
    public String toString() {
        String json = new Gson().toJson(this);
        return json;
    }

    /***
     * Konvertuje tranyakciu zo stringu do triedy Transaction
     * @param jsonTransaction tranzakcia vo formate Jsonu
     * @return vrati tranzakciu
     */
    @Nullable
    public static Transaction fromString(@NonNull String jsonTransaction){
        if(jsonTransaction.isEmpty()){
            return null;
        }

        Transaction transaction;
        transaction = new Gson().fromJson(jsonTransaction, Transaction.class);
        return transaction;
    }
}
