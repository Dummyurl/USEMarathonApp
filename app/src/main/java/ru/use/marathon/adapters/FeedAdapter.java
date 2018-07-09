package ru.use.marathon.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        holder.created_at.setText(feed.getCreatedAt(i));
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }


}
