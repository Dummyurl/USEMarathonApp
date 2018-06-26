package ru.use.marathon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.TheoryTopics;

/**
 * Created by ilyas on 14-Jun-18.
 */

public class TheoryTopicsAdapter extends RecyclerView.Adapter<TheoryTopicsAdapter.TheoryViewHolder> {


    TheoryTopics theoryTopics;

    public TheoryTopicsAdapter(TheoryTopics theoryTopics) {
        this.theoryTopics = theoryTopics;
    }

    @Override
    public TheoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_theory_item,parent,false);

        return new TheoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TheoryViewHolder holder, int position) {
        holder.theory_tv.setText(theoryTopics.getTitle(position));
    }

    @Override
    public int getItemCount() {
        return theoryTopics.size();
    }

    class TheoryViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.theory_title_txt)
        TextView theory_tv;

        public TheoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
