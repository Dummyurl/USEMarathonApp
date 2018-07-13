package ru.use.marathon.adapters.chat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import ru.use.marathon.R;
import ru.use.marathon.adapters.CollectionsAdapter;
import ru.use.marathon.models.chat.ChatRoom;
import ru.use.marathon.models.chat.Rooms;

/**
 * Created by ilyas on 08-Jul-18.
 */

public class ChatRoomsAdapter extends RecyclerView.Adapter<ChatRoomsAdapter.ChatViewHolder> {

    ArrayList<ChatRoom> roomArrayList;
    Context context;
    private  static String today;

    class ChatViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.chat_room_image) CircleImageView image;
        @BindView(R.id.chat_room_image_text) TextView image_text;
        @BindView(R.id.chat_room_title) TextView title;
        @BindView(R.id.chat_room_user_name) TextView username;
        @BindView(R.id.chat_room_message) TextView message;
        @BindView(R.id.chat_room_timestamp) TextView timestamp;
        @BindView(R.id.chat_room_message_count) TextView message_count;

        public ChatViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }


    public ChatRoomsAdapter(Context context, ArrayList<ChatRoom> rooms) {
        this.context = context;
        this.roomArrayList = rooms;
        Calendar calendar = Calendar.getInstance();

        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chat_rooms_item,parent,false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        ChatRoom rooms = roomArrayList.get(position);

        holder.image.setFillColor(getRandomMaterialColor("400"));
        holder.image_text.setText(rooms.getTitle().substring(0,1));
        holder.image_text.setAllCaps(true);
        holder.image_text.setTextColor(Color.WHITE);

        holder.title.setText(rooms.getTitle());

        if(rooms.getName().isEmpty() && rooms.getLastMessage().isEmpty())
            holder.username.setText("");
        else
            holder.username.setText(rooms.getName() + ": ");

        holder.message.setText(rooms.getLastMessage());

        if(!rooms.getTimestamp().isEmpty())
            holder.timestamp.setText(getTimeStamp(rooms.getTimestamp()));
        else
            holder.timestamp.setText("");

        if(rooms.getUnreadCount() > 0){
            holder.message_count.setVisibility(View.VISIBLE);
            holder.message_count.setText(String.valueOf(rooms.getUnreadCount()));
        }else{
            holder.message_count.setVisibility(View.INVISIBLE);
        }

    }
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = context.getResources().getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = context.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
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

    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }

}
