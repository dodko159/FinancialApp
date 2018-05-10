package cz.utb.fai.dodo.financialapp.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cz.utb.fai.dodo.financialapp.R;
import cz.utb.fai.dodo.financialapp.common.interfaces.IAdapterItemClicked;
import cz.utb.fai.dodo.financialapp.common.MyDate;
import cz.utb.fai.dodo.financialapp.common.NumberFormatter;
import cz.utb.fai.dodo.financialapp.common.Transaction;

/**
 * Created by Dodo on 13.04.2018.
 */

public class AdapterTransaction extends RecyclerView.Adapter<AdapterTransaction.ViewHolder>{

    private List<Transaction> listItems;
    private IAdapterItemClicked<Transaction> listener;

    public AdapterTransaction(List<Transaction> listItems, IAdapterItemClicked<Transaction> listener) {
        this.listItems = listItems;
        this.listener = listener;
    }

    @Override
    public AdapterTransaction.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_transaction,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Transaction transaction = listItems.get(position);
        String price = NumberFormatter.formateNumber(transaction.getPrice(), ' ') + " " + Transaction.CURRENCY;

        holder.date.setText(MyDate.longTimeToDate(transaction.getTransactionDate()));
        holder.price.setText(price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(transaction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void setNewList(List<Transaction> list){

        Collections.sort(list, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction o1, Transaction o2) {
                return o2.getTransactionDate().compareTo(o1.getTransactionDate());
            }
        });

        this.listItems=list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView date, price;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textViewTransactionDate);
            price = itemView.findViewById(R.id.textViewTransactionPrice);
        }
    }
}
