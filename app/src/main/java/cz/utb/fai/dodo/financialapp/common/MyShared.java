package cz.utb.fai.dodo.financialapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by Dodo on 27.03.2018.
 */

public class MyShared {

    /**** CONSTANTS ****/

    private static final String SHARED = "finShared";
    private static final String USERJSON = "userJson";
    private static final String INTERNETDIALOG = "internetDialogShown";
    public static final String ISINCOMES = "isInscomes";

    /**** METHODS *****/

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
     * @param context aktualny kontext
     * @param key kluc, podla ktoreho zmaze data
     */
    public static void clearSharedByKey(@NonNull Context context, String key){
        sGetPrefs(context).edit().remove(key).apply();
    }

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

    /**
     * Ukladá true alebo false do sharedpreferences pod kluc INTERNETDIALOG
     * @param context aplikacny kontext
     * @param shown true ak už bolo dialogove okno zobrazené false pri spustení aplikácie
     */
    public static void internetDialogShown(@NonNull Context context, boolean shown) {
        sGetPrefs(context).edit().putBoolean(INTERNETDIALOG, shown).apply();
    }

    /**
     * Nacita zo sharedpreferences, true alebo false podla toho, či už bolo dialogove okno zobrazene
     * @param context aplikacny kontext
     * @return true alebo false podla toho, či už bolo dialogove okno zobrazene
     */
    @NonNull
    public static Boolean wasInternetDialogShown(@NonNull Context context){
        return sGetPrefs(context).getBoolean(INTERNETDIALOG, false);
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
