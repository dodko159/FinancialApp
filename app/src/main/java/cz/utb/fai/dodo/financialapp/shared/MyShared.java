package cz.utb.fai.dodo.financialapp.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Dodo on 27.03.2018.
 */

public class MyShared {

    private static final String SHARED = "finShared";
    private static final String USERJSON = "userJson";
    public static final String ISINCOMES = "isInscomes";

    /**** ACCESSOR METHODS *****/
    /**
     * Uklada objekt {@link User} do shared prefs jako json.
     * @param context aktualni context
     * @param user user objekt
     */
    public static void setUser(@NonNull Context context, User user){
        setString(context, USERJSON, User.userToString(user));
    }

    /**
     *
     * @param context aktualni context
     * @return Vraci objekt {@link User}, ktery je ulozen ve shared prefs jako json.
     */
    public static User getUser(@NonNull Context context){
        User user = new User();
        String userjson = sGetPrefs(context).getString(MyShared.USERJSON,null);

        if(userjson == null){
            user = DBManager.loadUserFromAuth();
        }else{
            user.userFromJson(userjson);
        }

        return user;
    }

    /***
     *
     * @param context aktualni kontext
     * @param key kluc, podla ktoreho ma data ulozit
     * @param transactions tranzakcie, ktore sa maju ulozit
     */
    public static void saveTransactions(@NonNull Context context, String key, String transactions){
        setString(context, key, transactions);
    }


    //todo skontrolovat a asi zmazat
    /***
     * Nacita data z pamäte
     * @param context aktualny kontext
     * @param key kluc, podla ktoreho nacita data z pamäti
     * @return vrati data v podobe mapy < kluc, List< Transaction >> alebo null ak data niesu k dispozicii
     */
    public static List<Transaction> loadTransactions(@NonNull Context context, String key) {
        List<Transaction> myList = null;
        String transJson = sGetPrefs(context).getString(key,null);

        if(transJson != null){
           // myList = Transaction.transactionListFromFirebase(transJson);
        }

        return myList;
    }

    /***
     *
     * @param context aktualny kontext
     * @param key kluc, podla ktoreho zmaze data
     */
    public static void clearSharedByKey(@NonNull Context context, String key){
        sGetPrefs(context).edit().remove(key).apply();
    }

    /***** HELPER METHODS *****/

    /**
     * Uklada string value na dany klic.
     * @param context aktualni context
     * @param key klic
     * @param value value
     */
    private static void setString(@NonNull Context context, @NonNull String key, String value) {
        sGetPrefs(context).edit().putString(key, value).apply();
    }

    /**
     *
     * @param context kontext
     * @param isIncomes boolean ci sa ma nacitat incomes alebo costs
     */
    public static void setIsIncomes(@NonNull Context context, boolean isIncomes){
        sGetPrefs(context).edit().putBoolean(ISINCOMES,isIncomes).apply();
    }

    /***
     * nacita hodnotu z sharedPreferences
     * @param context kontext
     * @return boolean ci sa ma nacitat incomes alebo costs
     */
    public static boolean getIsIncomes(@NonNull Context context){
        return sGetPrefs(context).getBoolean(ISINCOMES, false);
    }

    /***
     *
     * @param context aktualny kontext
     * @return vrati instanciu SharedPreferences
     */
    public static SharedPreferences sGetPrefs(@NonNull Context context) {
        return context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    }
}
