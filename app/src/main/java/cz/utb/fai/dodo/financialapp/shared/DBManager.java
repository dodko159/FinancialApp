package cz.utb.fai.dodo.financialapp.shared;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dodo on 27.03.2018.
 */

public class DBManager{

    /***** CONSTANTS *****/
    private static final String USERS = "users";
    private static final String INCOMES = Transaction.INCOMES;
    private static final String COSTS = Transaction.COSTS;

    private static DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(USERS);
    private static DatabaseReference incomeRef = FirebaseDatabase.getInstance().getReference(INCOMES);
    private static DatabaseReference costRef = FirebaseDatabase.getInstance().getReference(COSTS);

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /**** ****/
    /***
     * Uklada uzivatela do DB
     * @param user uzivatel, ktoreho ma ulozit
     */
    public static void saveUserToDB(User user){
        userRef.child(user.getUid()).setValue(user);
    }

    /***
     * Updatuje data uzivatela
     * @param user uzivatel, ktoreho ma ulozit
     */
    public static void updateUserInDB(User user){
        userRef.child(user.getUid()).setValue(user);
    }

    //todo upravit - nacitat z DB a az potom ak nieje z auth
    /**
     *
     * @return Vrati prihlaseneho uzivatela
     */
    @NonNull
    public static User loadUserFromDB(){
        //todo upravit
        return new User(firebaseAuth.getCurrentUser());
    }

    /***
     * Skotroluje existenciu uzivatela v databazi
     * @param uid id uzivatela
     * @return true ak je uzivatel v databazi false ak nieje
     */
    @NonNull
    public static Boolean ckeckUserInDB(String uid){

        return checkDataInDB(userRef.child(uid));
    }

    /***
     * Ulozi data do databaze
     * @param userUid id uzivatela
     * @param transaction tranzkcia, ktora sa ma ulozit
     * @param income true ak sa jedna on prijem (Incomes) flase ak o vidaj (Costs)
     */
    public static void saveTransactionToDB(String userUid, Transaction transaction, Boolean income){
        DatabaseReference dbRef = incomeRef;
        if(!income){
            dbRef = costRef;
        }

        String monthYear = MyDate.longTimeToMonthYear(transaction.getTransactionDate());

        DatabaseReference ref = dbRef.child(userUid)
                .child(monthYear)
                .push();

        transaction.setUid(ref.getKey());

        ref.setValue(transaction);
    }

    // todo upravit
    /***
     * Nacita tranzakcie s databaze
     * @param context aktualny kontext
     * @param userUid id uzivatela
     * @param selectedMonth
     * @param income true ak sa jedna on prijem (Incomes) flase ak o vidaj (Costs)
     */
    public static void loadTransactionsFromDB(@NonNull final Context context, String userUid, String selectedMonth, Boolean income) {
        DatabaseReference dbRef = incomeRef;
        String key = INCOMES;
        if (!income) {
            dbRef = costRef;
            key = COSTS;
        }

        final String key2 = key;

            dbRef.child(userUid).child(selectedMonth).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MyShared.saveTransactions(context, key2, dataSnapshot.getValue().toString());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
                }
            });
    }

    //todo: skontrolovat
    /***
     * skontroluje, ci su data v databazi
     * @param dbRef referencia na uzol v DB
     * @return vracia true ak databaza nieje prazdna
     */
    @NonNull
    private static Boolean checkDataInDB(DatabaseReference dbRef){
        final boolean[] isInDB = {false};

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isInDB[0] = dataSnapshot.exists();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
            }
        });

        return isInDB[0];
    }
}