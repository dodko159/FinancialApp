package cz.utb.fai.dodo.financialapp.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Created by Dodo on 27.03.2018.
 */

public class MyShared {

    private static final String SHARED = "finShared";
    private static final String USERJSON = "userJson";

    /**** ACCESSOR METHODS *****/
    /**
     * Uklada objekt {@link User} do shared prefs jako json.
     * @param context aktualni context
     * @param user user objekt
     */
    public static void setUser(@NonNull Context context, User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);

        setString(context, USERJSON, json);
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
            user = new User();
            user.loadUserFromDB();
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

    /***
     * Nacita data z pamäte
     * @param context aktualny kontext
     * @param key kluc, podla ktoreho nacita data z pamäti
     * @return vrati data v podobe mapy < kluc, List< Transaction >> alebo null ak data niesu k dispozicii
     */
    public static Map<String, List<Transaction>> loadTransactions(@NonNull Context context, String key) {
        Map<String, List<Transaction>> myMap = null;
        String transJson = sGetPrefs(context).getString(key,null);

        if(transJson != null){
            myMap = Transaction.transactionMapFromJson(transJson);
        }

        return myMap;
    }

    /***
     *
     * @param context aktualny kontext
     * @param key kluc, podla ktoreho zmaze data
     */
    public static void clerSharedByKey(@NonNull Context context, String key){
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

    /***
     *
     * @param context aktualny kontext
     * @return vrati instanciu SharedPreferences
     */
    private static SharedPreferences sGetPrefs(@NonNull Context context) {
        return context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
    }
}
