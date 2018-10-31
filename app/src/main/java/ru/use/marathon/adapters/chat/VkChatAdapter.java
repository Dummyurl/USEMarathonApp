package ru.use.marathon.adapters.chat;

import android.arch.lifecycle.HolderFragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.fragments.ChatRoomFragment.Message;
import ru.use.marathon.models.chat.Messages;

public class VkChatAdapter extends RecyclerView.Adapter<VkChatAdapter.VkVievHolder> {
    private Context context;
    private Messages msgs;

    @NonNull
    @Override
    public VkVievHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_message_item, parent, false);
        return new VkVievHolder(v);
    }

// TODO: Реализовать ввод имени
    @Override
    public void onBindViewHolder(@NonNull VkVievHolder holder, int position) {
        Message message = msgs.getMessage(position);
        holder.text.setText(message.getMsg());
        if(message.isOut()){
            holder.layout.setGravity(Gravity.RIGHT);
            holder.name.setVisibility(View.GONE);
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.tab_white));
            holder.text.setTextColor(context.getResources().getColor(R.color.cardview_dark_background));
        }else{
            holder.layout.setGravity(Gravity.LEFT);
            holder.name.setText(message.getUser_name());
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.text.setTextColor(context.getResources().getColor(R.color.tab_white));
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.chatCard));
        }
        holder.time.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public VkChatAdapter(Context context, Messages messages) {
        this.context = context;
        this.msgs = messages;
    }

    class VkVievHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.chat_name) TextView name;
        @BindView(R.id.chat_messages_tv) TextView text;
        @BindView(R.id.chat_timestamp) TextView time;
        @BindView(R.id.message_item_layout) LinearLayout layout;
        @BindView(R.id.msg_card) LinearLayout cardView;


    public VkVievHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


}
}
