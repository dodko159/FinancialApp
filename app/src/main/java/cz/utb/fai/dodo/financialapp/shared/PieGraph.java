package cz.utb.fai.dodo.financialapp.shared;

import android.graphics.Color;

import com.razerdp.widget.animatedpieview.data.IPieInfo;

/**
 * Created by Dodo on 20.04.2018.
 */

public class PieGraph implements IPieInfo {

    private float percentage;
    private int color;
    private String description;

    public PieGraph(CategorySimple categorySimple, double sum, int color) {
        this.percentage = ((float) categorySimple.getPriceSum() / (float) sum) * 100;
        this.color = color;
        this.description = Category.getCategoryName(categorySimple.getCategory());
    }

    @Override
    public double getValue() {
        return percentage;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public String getDesc() {
        return description;
    }
}
