package ru.use.marathon.adapters.tasks_creation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.TaskCreation.TaskAnswer;

/**
 * Created by ilyas on 14-Jul-18.
 */
public class TaskAnswersAdapter extends RecyclerView.Adapter<TaskAnswersAdapter.AnswersViewHolder> {

    private final Context context;
    private List<TaskAnswer> items;

    public TaskAnswersAdapter(List<TaskAnswer> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public AnswersViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_task_answer_item, parent, false);
        return new AnswersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AnswersViewHolder holder, final int position) {
        holder.answer.setText(items.get(position).getContent());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,items.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class AnswersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.answer_content)
        TextView answer;
        @BindView(R.id.delete_btn)
        ImageButton delete;

        public AnswersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}