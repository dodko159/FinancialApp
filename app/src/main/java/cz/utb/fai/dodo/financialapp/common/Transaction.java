package cz.utb.fai.dodo.financialapp.common;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
        return new Gson().toJson(this);
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

    /***
     * Prelozi data z Firebase
     * @param dataSnapshot tranzakcie vo formate DataSnapshot
     * @return vrati data v podobe List< Transaction >
     */
    public static List<Transaction> transactionListFromFirebase(DataSnapshot dataSnapshot) {

        List<Transaction> transactions = new ArrayList<>();

        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
            Transaction transaction = snapshot.getValue(Transaction.class);
            transactions.add(transaction);
        }

        return transactions;
    }

}
