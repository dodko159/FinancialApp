package cz.utb.fai.dodo.financialapp.common;

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

/**
 * Created by Dodo on 27.03.2018.
 */

public class DBManager{

    /***** CONSTANTS *****/
    private static final String USERS = "users";
    private static final String INCOMES = Transaction.INCOMES;
    private static final String COSTS = Transaction.COSTS;

    /**** VARS ****/

    private static DatabaseReference userRef;
    public static DatabaseReference incomeRef;
    public static DatabaseReference costRef;

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    /**** METHODS ****/

    /***
     * inicializuje DB
     */
    public static void initDB(){
        FirebaseDatabase fire = FirebaseDatabase.getInstance();
        fire.setPersistenceEnabled(true);

        userRef = fire.getReference(USERS);
        incomeRef = fire.getReference(INCOMES);
        costRef = fire.getReference(COSTS);
    }

    /***
     * Uklada uzivatela do DB
     * @param user uzivatel, ktoreho ma ulozit
     */
    private static void saveUserToDB(User user){
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
    static User loadUserFromAuth(){
        return new User(firebaseAuth.getCurrentUser());
    }

    /***
     * Skotroluje existenciu uzivatela v databazi a uloží ho ak tam nieje
     * @param user firebase užívatel
     * @param context kontext
     */
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
     * @param transaction transakcia, ktora sa ma ulozit
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

    /**
     * Zmeaže danú transakciu
     * @param transaction transakcia, ktorú má vymazať
     */
    public static void removeTransaction(String uid, Transaction transaction) {
        String date = MyDate.longTimeToMonthYear(transaction.getTransactionDate());
        String transID = transaction.getUid();
        DatabaseReference dbRef;

        if(transaction.getCategory() < Category.OFFSET){
            dbRef = DBManager.incomeRef;
        }else{
            dbRef = DBManager.costRef;
        }

        dbRef.child(uid).child(date).child(transID).removeValue();
    }
}