package cz.utb.fai.dodo.financialapp.shared;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Trieda obsahujúca názov kategórie a celkovú sumu.
 * Obsahuje taktiež funkciu na konverziu Mapy < Integer, Double > na list.
 */

public class CategorySimple {
    /**** VARS ****/
    private int category;
    private String priceSum;

    /**** CONSTRUCTOR ****/
    public CategorySimple(int category, Double priceSum) {
        this.category = category;
        this.priceSum = priceSum+"";
    }

    /**** GETS *****/
    public int getCategory() {
        return category;
    }

    public String getPriceSum() {
        return priceSum;
    }

    /**** CONVERSION METHODS ****/

    public static List<CategorySimple> mapToList(@NonNull Map<Integer, Double> map){
        List<CategorySimple> newList = new ArrayList<>();

        for(Map.Entry<Integer, Double> entry : map.entrySet()) {
            Integer key = entry.getKey();
            Double value = entry.getValue();

            newList.add(new CategorySimple(key, value));
        }

        return newList;
    }
}
