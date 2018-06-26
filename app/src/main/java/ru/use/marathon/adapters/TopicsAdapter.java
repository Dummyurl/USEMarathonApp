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
import ru.use.marathon.models.topics.SolvedTestsByTopics;
import ru.use.marathon.models.topics.Topics;

/**
 * Created by ilyas on 20-Jun-18.
 */

public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.TopicsViewHolder> {

    Topics topics;
    SolvedTestsByTopics solvedTestsByTopics;


    public TopicsAdapter(Topics topics, SolvedTestsByTopics solvedTestsByTopics) {
        this.topics = topics;
        this.solvedTestsByTopics = solvedTestsByTopics;
    }


    @Override
    public TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_topic_item, parent, false);

        return new TopicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TopicsViewHolder holder, int position) {
        holder.content_tv.setText(topics.getContent(position));

        for (int i = 0; i < solvedTestsByTopics.size(); i++) {
            if (topics.getID(position) == solvedTestsByTopics.getTopicID(i)) {
                holder.progressBar.setMax(topics.getTestsNumber(position));
                holder.progressBar.setProgress(solvedTestsByTopics.getSolvedTestsNumber(i));
                holder.pb_info_tv.setText(String.valueOf(solvedTestsByTopics.getSolvedTestsNumber(i)) + "/" + String.valueOf(topics.getTestsNumber(position)));
            }
        }

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
