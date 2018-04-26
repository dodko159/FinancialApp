package cz.utb.fai.dodo.financialapp.shared;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.security.PublicKey;
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

    /**** CONSTANTS ****/
    public static final String INCOMES = "incomes";
    public static final String COSTS = "costs";
    public static final String CURRENCY = "CZK";

    /**** VARS ****/
    private String uid;
    private long transactionDate;
    private long creationDate;
    private int category;
    private double price;
    private String description;

    /****  CONSTRUCTOR ****/

    public Transaction(){}

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

    public Long getTransactionDate() {
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

    /**** CONVERSION METHODS ****/

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

    /**
     * Skonvertuje list tranzakcii na json
     * @param transactions List tranzakcii na skonvertovanie
     * @return List vo forme Json stringu
     */
    public static String transactionsToString(List<Transaction> transactions){
        return new Gson().toJson(transactions);
    }

    /**
     * Skonvertuje Json string na List tranzakcii
     * @param trasJson Json tranzakcii
     * @return List tranzakcii
     */
    public static List<Transaction> transactionListFromJson(@NonNull String trasJson){
        Type fooType = new TypeToken<List<Transaction>>() {}.getType();

        return new Gson().fromJson(trasJson,fooType);
    }

    // TODO ASI ZNAZAT
    /***
     * Prelozi data z Jsonu
     * @param transJson tranzakcie vo formate Json (String)
     * @return vrati data v podobe mapy < kluc, List< Transaction >>
     */
    @Nullable
    public static Map<String, List<Transaction>> transactionMapFromFirebaseJson2(@NonNull String transJson) {
        Map<String, LinkedTreeMap> myMap;
        Map<String, List<Transaction>> mapList = new HashMap<>();

        myMap = new Gson().fromJson(transJson, HashMap.class);

        for(Map.Entry<String, LinkedTreeMap> entry : myMap.entrySet()){
            LinkedTreeMap month = entry.getValue();
            List transactions = new ArrayList<Transaction>(month.values());

            Type fooType = new TypeToken<List<Transaction>>() {}.getType();
            List<Transaction> transList = new Gson().fromJson(transactions.toString(),fooType);

            mapList.put(entry.getKey(),transList);
        }

        return mapList;
    }

    /***
     * Prelozi data z Firebase
     * @param dataSnapshot tranzakcie vo formate DataSnapshot
     * @return vrati data v podobe List< Transaction >
     */
    @Nullable
    public static List<Transaction> transactionListFromFirebase(DataSnapshot dataSnapshot) {

        List<Transaction> transactions = new ArrayList<>();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Transaction transaction = snapshot.getValue(Transaction.class);
            transactions.add(transaction);
        }

        return transactions;
    }

}
