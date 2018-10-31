package ru.use.marathon.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.models.SheetsRating;

/**
 * Created by ilyas on 13-Oct-18.
 */

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    private Context context;
    private ArrayList<SheetsRating> ratings;

    public RatingAdapter(Context context, ArrayList<SheetsRating> ratings) {
        this.context = context;
        this.ratings = ratings;

        Collections.sort(ratings, new Comparator<SheetsRating>() {
            @Override
            public int compare(SheetsRating rating, SheetsRating t1) {
                return t1.getTotal() - rating.getTotal();
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 1 || position == 2) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rating_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rating_item_secondary, parent, false);
        }
        return new RatingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder h, int position) {
        SheetsRating r = ratings.get(position);
        h.order.setText(String.valueOf(position + 1));
        h.fullname.setText(r.getFullname());
        h.score.setText(String.valueOf(r.getTotal()));

        h.order.setTextColor(Color.BLACK);
        h.score.setTextColor(Color.BLACK);
        h.fullname.setTextColor(Color.BLACK);

        if (h.getItemViewType() == 0) {
            if (position == 0) {
                h.card.setBackgroundColor(Color.rgb(244, 212, 66)); // gold
            } else if (position == 1) {
                h.card.setBackgroundColor(Color.rgb(224, 224, 224)); // silver
            } else if (position == 2) {
                h.card.setBackgroundColor(Color.rgb(168, 143, 63)); // bronze
            }
        }
        h.card.setCardElevation(6);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    class RatingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rating_item_card_layout)
        LinearLayout card_layout;
        @BindView(R.id.rating_item_cardview)
        CardView card;
        @BindView(R.id.rating_item_order)
        TextView order;
        @BindView(R.id.rating_item_fullname)
        TextView fullname;
        @BindView(R.id.rating_item_score)
        TextView score;

        public RatingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}