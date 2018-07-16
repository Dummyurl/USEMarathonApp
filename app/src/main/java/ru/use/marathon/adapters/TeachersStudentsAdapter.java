package ru.use.marathon.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.use.marathon.R;
import ru.use.marathon.models.UsersResponse;

/**
 * Created by ilyas on 16-Jul-18.
 */
public class TeachersStudentsAdapter extends RecyclerView.Adapter<TeachersStudentsAdapter.MyStudentsViewHolder> {
    private final Context context;
    private UsersResponse items;

    public TeachersStudentsAdapter(UsersResponse items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public MyStudentsViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_users_item, parent, false);
        return new MyStudentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyStudentsViewHolder holder, int position) {
        holder.button.setVisibility(View.GONE);
        holder.id.setText(String.valueOf(items.getId(position)));
        holder.name.setText(items.getName(position));
        if(!items.getImage(position).isEmpty())
            Picasso.get().load(items.getImage(position)).resize(100,100).centerCrop().into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class MyStudentsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.users_image)
        CircleImageView imageView;
        @BindView(R.id.users_name)
        TextView name;
        @BindView(R.id.users_id) TextView id;
        @BindView(R.id.users_select_teacher)
        RadioButton button;

        public MyStudentsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}