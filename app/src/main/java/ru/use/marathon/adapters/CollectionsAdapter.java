package ru.use.marathon.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.Collections;

/**
 * Created by ilyas on 10-Jun-18.
 */

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.VariantsViewHolder> {

    Collections collection_numbers;

    public CollectionsAdapter(Collections collection_numbers) {
        this.collection_numbers = collection_numbers;
    }

    @Override
    public VariantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_collection_item,parent,false);

        return new VariantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VariantsViewHolder holder, int position) {
        holder.collection_number.setText(Integer.toString(collection_numbers.collection_number(position)));
    }

    @Override
    public int getItemCount() {
        return collection_numbers.size();
    }

    class VariantsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.collection_cardview)
        CardView collection_cv;
        @BindView(R.id.collection_number)
        TextView collection_number;

        public VariantsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
