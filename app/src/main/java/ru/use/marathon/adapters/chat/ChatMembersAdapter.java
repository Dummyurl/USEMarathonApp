package ru.use.marathon.adapters.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.use.marathon.R;
import ru.use.marathon.models.chat.ChatMembers;

/**
 * Created by ilyas on 13-Jul-18.
 */
public class ChatMembersAdapter extends RecyclerView.Adapter<ChatMembersAdapter.MembersViewHolder> {
    private final Context context;
    private ChatMembers items;

    public ChatMembersAdapter(ChatMembers items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public MembersViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_users_item, parent, false);
        return new MembersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MembersViewHolder holder, int position) {
        holder.checkBox.setVisibility(View.GONE);
        holder.rbtn.setVisibility(View.GONE);
        if(!items.getImage(position).isEmpty()) Picasso.get().load(items.getImage(position)).resize(100,100).centerCrop().into(holder.image);
        holder.name.setText(items.getName(position));
        holder.id.setText(String.valueOf(items.getId(position)));
        holder.id.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class MembersViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.users_image)
        CircleImageView image;
        @BindView(R.id.users_id)
        TextView id;
        @BindView(R.id.users_name) TextView name;
        @BindView(R.id.users_select)
        CheckBox checkBox;
        @BindView(R.id.users_select_teacher)
        RadioButton rbtn;


        public MembersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}