package ru.use.marathon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.topics.Topics;

/**
 * Created by ilyas on 20-Jun-18.
 */

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder> {

    Topics topics;

    public TopicsAdapter(Topics topics) {
        this.topics = topics;
    }

    @Override
    public TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_item, parent, false);

        return new TopicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicsViewHolder holder, int position) {
        holder.content_tv.setText(topics.getTitle(position));

        holder.progressBar.setMax(topics.getAmount(position));
        holder.progressBar.setProgress(topics.getSolved(position));
        holder.pb_info_tv.setText(String.valueOf(topics.getSolved(position)) + "/" + String.valueOf(topics.getAmount(position)));

    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    class TopicsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.topics_content_txt)
        TextView content_tv;
        @BindView(R.id.tests_number_progress)
        ProgressBar progressBar;
        @BindView(R.id.progress_info_txt)
        TextView pb_info_tv;


        public TopicsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
