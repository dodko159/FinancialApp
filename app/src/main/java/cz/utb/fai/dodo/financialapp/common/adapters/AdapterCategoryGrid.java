package cz.utb.fai.dodo.financialapp.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;

/**
 * Created by Dodo on 17.04.2018.
 */

public class AdapterCategoryGrid extends RecyclerView.Adapter<AdapterCategoryGrid.ViewHolder> {

    /**** VARS ****/

    private List<String> categories;
    private IAdapterItemClicked<Integer> listener;
    private Context context;

    /**** CONSTRUCTOR ****/

    public AdapterCategoryGrid(String[] categories, IAdapterItemClicked<Integer> listener, Context applicationContext) {
        this.categories = Arrays.asList(categories);
        this.listener = listener;
        this.context = applicationContext;
    }

    /**** MTEHODS ****/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_category_grid, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String catName = categories.get(position);

        if(position%2 == 0){
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.backgroundColor1));
        }else{
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorGrayPink));
        }

        holder.catName.setText(catName);

        final int pos = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setNewList(String[] list){
        this.categories=Arrays.asList(list);
        notifyDataSetChanged();
    }

     /**** INNER CLASS ****/

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView catName;

        ViewHolder(View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.textViewGridRecycleViewCategory);
        }
    }
}
