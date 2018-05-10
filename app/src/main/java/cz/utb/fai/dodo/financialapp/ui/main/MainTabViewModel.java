package cz.utb.fai.dodo.financialapp.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import cz.utb.fai.dodo.financialapp.common.MyShared;
import cz.utb.fai.dodo.financialapp.common.Transaction;

/**
 * Created by Dodo on 24.04.2018.
 */

public class MainTabViewModel extends AndroidViewModel {

    /**** CONSTANTS ****/
    private boolean INCOMES = true;
    private boolean COSTS = false;

    /***** VARS *****/
    private DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference(Transaction.INCOMES);
    private DatabaseReference costRef = FirebaseDatabase.getInstance().getReference(Transaction.COSTS);

    private MutableLiveData<List<String>> months = new MutableLiveData<>();
    private List<String> incomeMoths = new ArrayList<>();
    private List<String> costMonths = new ArrayList<>();
    private String userID;

    /***** CONSTRUCTOR *****/
    public MainTabViewModel(@NonNull Application application) {
        super(application);
        this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    /***** GETS, SETS ******/

    private void setCostMonths(List<String> costMonths) {
        this.costMonths = costMonths;
    }

    private void setIncomeMoths(List<String> incomeMoths) {
        this.incomeMoths = incomeMoths;
    }

    MutableLiveData<List<String>> getMonths() {
        return months;
    }

    /***** HELPER METHODS *****/
    void init(){
        ValueEventListener listenerForIncomes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> keys = dataSnapshotToListKeys(dataSnapshot.getValue());

                    setIncomeMoths(keys);
                    months.setValue( joinMonths());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
            }
        };

        ValueEventListener listenerForCosts = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> keys = dataSnapshotToListKeys(dataSnapshot.getValue());

                   // if(months.getValue() == null || months.getValue().size() == 0){

                    setCostMonths(keys);
                    months.setValue( joinMonths());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
            }
        };

        incomeRef.child(userID).addListenerForSingleValueEvent(listenerForIncomes);
        costRef.child(userID).addListenerForSingleValueEvent(listenerForCosts);
    }

    private List<String> joinMonths() {
        List<String> allMoths = new ArrayList<>();

        allMoths.addAll(costMonths);
        allMoths.addAll(incomeMoths);

        allMoths = new ArrayList<>(new LinkedHashSet<>(allMoths));

        Collections.sort(allMoths);

        return allMoths;
    }

    /***
     * Vraty kluce z firebase DataSnapshot
     * @param value DataSnapshot z firebase
     * @return list klucov
     */
    private List<String> dataSnapshotToListKeys(Object value) {
        HashMap<String, Object> data = (HashMap<String, Object>) value;
        List<String> keys = new ArrayList<>();
        keys.addAll(data.keySet());
        return keys;
    }

    public void switchToIncomes(View v){
        MyShared.setIsIncomes(this.getApplication(), INCOMES);
    }

    public void switchToCosts(View v){
        MyShared.setIsIncomes(this.getApplication(), COSTS);
    }
}
