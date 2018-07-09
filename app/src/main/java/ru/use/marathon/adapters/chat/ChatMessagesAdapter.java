package ru.use.marathon.adapters.chat;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.chat.Message;

/**
 * Created by ilyas on 09-Jul-18.
 */

public class ChatMessagesAdapter  extends RecyclerView.Adapter<ChatMessagesAdapter.MessagesViewHolder> {


    ArrayList<Message> messageArrayList;
    String user_id;
    int user_type;
    static String today;


    public ChatMessagesAdapter(ArrayList<Message> messageArrayList,String user_id,int user_type) {
        this.messageArrayList = messageArrayList;
        this.user_id = user_id;
        this.user_type = user_type;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_message_item,parent,false);

        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        Message m = messageArrayList.get(position);
        if(Objects.equals(m.getUid(), user_id) && user_type ==m.getUser_type()){
            holder.item_layout.setGravity(Gravity.RIGHT);
        }else{
            holder.item_layout.setGravity(Gravity.LEFT);
        }
        holder.messages.setText(m.getText());
        holder.timestamp.setText(getTimeStamp(m.getTimestamp()));
        holder.name.setText(m.getName());

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    private static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.message_item_layout)
        LinearLayout item_layout;
        @BindView(R.id.chat_messages_tv)
        TextView messages;
        @BindView(R.id.chat_timestamp)
        TextView timestamp;
        @BindView(R.id.chat_name)
        TextView name;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
