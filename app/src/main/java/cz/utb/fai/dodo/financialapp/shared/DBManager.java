package cz.utb.fai.dodo.financialapp.shared;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.ui.main.MainTabActivity;

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
    public static User loadUserFromAuth(){
        return new User(firebaseAuth.getCurrentUser());
    }

    /***
     * Skotroluje existenciu uzivatela v databazi
     * @param user
     * @return true ak je uzivatel v databazi false ak nieje
     */
    @NonNull
    public static void ckeckUserInDBandSave(FirebaseUser user, Context context){

        final User me = new User(user);
        final Context con = context;

        userRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User loadedUser;

                if(dataSnapshot.exists()){
                    loadedUser = dataSnapshot.getValue(User.class);
                    Toast.makeText(con, R.string.welcome_back,Toast.LENGTH_SHORT).show();
                }else {
                    saveUserToDB(me);
                    loadedUser = me;
                }

                MyShared.setUser(con,loadedUser);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("dbError", "loadPost:onCancelled", databaseError.toException());
            }
        });

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

    // todo upravit - mozno aj zmazat
    /***
     * Nacita tranzakcie s databaze
     * @param context aktualny kontext
     * @param userUid id uzivatela
     * @param selectedMonth mesiac, ktory sa ma stiahnut
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

    /***
     * Nacita tranzakcie s databaze
     * @param context aktualny kontext
     * @param userUid id uzivatela
     * @param selectedMonth
     * @param income true ak sa jedna on prijem (Incomes) flase ak o vidaj (Costs)
     */
    public static void loadTransactionsFromDBOld(@NonNull final Context context, String userUid, String selectedMonth, Boolean income) {
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
}