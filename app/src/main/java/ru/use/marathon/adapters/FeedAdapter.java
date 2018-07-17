package ru.use.marathon.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.Feed;

/**
 * Created by ilyas on 02-Jul-18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedViewHolder> {

    public static final String TAG = FeedAdapter.class.getSimpleName();
    Feed feed;

    private  static String today;

    class FeedViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.feed_image_item) ImageView imageView;
        @BindView(R.id.feed_text_item) TextView text;
        @BindView(R.id.feed_title_item) TextView title;
        @BindView(R.id.feed_id_item) TextView id;
        @BindView(R.id.feed_created_at) TextView created_at;

        public FeedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public FeedAdapter(Feed feed) {
        this.feed = feed;
        Calendar calendar = Calendar.getInstance();

        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed_item,parent,false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int i) {
        holder.id.setText(String.valueOf(feed.getId(i)));

        Log.d(TAG, "onBindViewHolder: " + feed.getImage(i));
        String image = feed.getImage(i);
        if(!image.isEmpty()){
            Picasso.get().load(image).into(holder.imageView);
        }

        holder.text.setText(feed.getContent(i));

        holder.title.setText(feed.getTitle(i));
        holder.created_at.setText(getTimeStamp(feed.getCreatedAt(i)));
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
        return feed.size();
    }


}
