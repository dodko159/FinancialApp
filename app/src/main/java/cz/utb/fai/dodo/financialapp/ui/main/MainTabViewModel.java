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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import cz.utb.fai.dodo.financialapp.shared.MyShared;
import cz.utb.fai.dodo.financialapp.shared.Transaction;
import cz.utb.fai.dodo.financialapp.shared.User;

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
    //private MutableLiveData<List<String>> costsMonths = new MutableLiveData<>();
    private List<String> incomeMoths = new ArrayList<>();
    private List<String> costMonths = new ArrayList<>();

    private User user;
    private boolean mothsDownloaded = false;
    private Context context;

    /***** CONSTRUCTOR *****/
    public MainTabViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.user = MyShared.getUser(context);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    /***** GETS, SETS ******/

    public void setCostMonths(List<String> costMonths) {
        this.costMonths = costMonths;
    }

    public void setIncomeMoths(List<String> incomeMoths) {
        this.incomeMoths = incomeMoths;
    }

    public MutableLiveData<List<String>> getMonths() {
        return months;
    }

    /***** HELPER METHODS *****/
    void init(){
        ValueEventListener listenerForIncomes = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> keys = dataSnapshotToListKeys(dataSnapshot.getValue());

                    if(mothsDownloaded){
                        keys = joinMonths(keys);
                        Collections.sort(keys);
                        months.setValue(keys);
                    }else {
                        setIncomeMoths(keys);
                        mothsDownloadedTrue();
                    }
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

                    if(mothsDownloaded){
                        keys = joinMonths(keys);
                        Collections.sort(keys);
                        months.setValue(keys);
                    }else {
                        setCostMonths(keys);
                        mothsDownloadedTrue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
            }
        };

        incomeRef.child(user.getUid()).addListenerForSingleValueEvent(listenerForIncomes);
        costRef.child(user.getUid()).addListenerForSingleValueEvent(listenerForCosts);
    }

    private List<String> joinMonths(List<String> moths) {
        List<String> allMoths = new ArrayList<>(moths);

        allMoths.addAll(costMonths);
        allMoths.addAll(incomeMoths);

        return new ArrayList<>(new LinkedHashSet<>(allMoths));
    }

    private void mothsDownloadedTrue() {
        mothsDownloaded = true;
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
        MyShared.setIsIncomes(context, INCOMES);
    }

    public void switchToCosts(View v){
        MyShared.setIsIncomes(context, COSTS);
    }
}
