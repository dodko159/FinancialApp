package cz.utb.fai.dodo.financialapp.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.common.Category;
import cz.utb.fai.dodo.financialapp.common.CategorySimple;
import cz.utb.fai.dodo.financialapp.common.NumberFormatter;
import cz.utb.fai.dodo.financialapp.common.Transaction;

/**
 * Created by Dodo on 13.04.2018.
 */

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder>{

    private List<CategorySimple> items;
    private IAdapterItemClicked<CategorySimple> listener;

    public AdapterCategory(List<CategorySimple> items, IAdapterItemClicked<CategorySimple> listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CategorySimple categorySimple = items.get(position);

        String categoryName = Category.getCategoryName(categorySimple.getCategory());
        String sum = NumberFormatter.formateNumber(categorySimple.getPriceSum(), ' ') + " " + Transaction.CURRENCY;

        holder.categoryName.setText(categoryName);
        holder.priceSum.setText(sum);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(categorySimple);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setNewList(List<CategorySimple> list){
        this.items=list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView categoryName, priceSum;

        ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            priceSum = itemView.findViewById(R.id.textViewPriceSum);
        }
    }
}