package ru.use.marathon.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.Statistics;

/**
 * Created by ilyas on 17-Jul-18.
 */
public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder> {

    private Double[] items;
    private String[] names;

    public StatisticsAdapter(Double[] items,String[] names) {
        this.items = items;
        this.names = names;
    }

    @Override
    public StatisticsViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_statistics_item, parent, false);
        return new StatisticsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatisticsViewHolder holder, int position) {
        double val = items[position];
        int v = (int) val;
        double error = val - v;
        if(error > 0){
            DecimalFormat numberFormat = new DecimalFormat("#.00");

            holder.num.setText(String.valueOf(numberFormat.format(items[position])) + "\ncек.");
        }else{
            holder.num.setText(String.valueOf(v));
        }
        holder.text.setText(names[position]);

        if(names.length == position+1){
            holder.num.setText(String.valueOf(v) + "%");
        }
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return names.length;
    }

    class StatisticsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.stat_number) TextView num;
        @BindView(R.id.stat_text) TextView text;

        public StatisticsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}