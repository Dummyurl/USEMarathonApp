package ru.use.marathon.fragments;

import android.graphics.Typeface;
import android.media.Rating;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.adapters.RatingAdapter;
import ru.use.marathon.interfaces.SheetsResult;
import ru.use.marathon.models.SheetsRating;
import ru.use.marathon.utils.GetSheetsRating;

import static ru.use.marathon.Constants.SHEETS_KEY;

/**
 * Created by ilyas on 10-Oct-18.
 */

public class RatingFragment extends Fragment {

    public static final String TAG = RatingFragment.class.getSimpleName();

    @BindView(R.id.rating_title) TextView title;
    @BindView(R.id.rating_progress) ProgressBar progress;
    @BindView(R.id.rating_rv) RecyclerView recyclerView;

    private ArrayList<SheetsRating> ratings;
    private RatingAdapter adapter;

    public RatingFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_rating, container, false);
        ButterKnife.bind(this, view);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gilroy.otf");
        title.setTypeface(font);


        progress.setVisibility(View.VISIBLE); initTable(); progress.setVisibility(View.GONE);

        return view;
    }

    private void initTable() {
        ratings = new ArrayList<>();
        new GetSheetsRating(new SheetsResult() {
            @Override
            public void onResult(JSONObject object) {
                ratings = processJson(object);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                adapter = new RatingAdapter(getActivity(),ratings);
                recyclerView.setAdapter(adapter);
            }
        }).execute("https://spreadsheets.google.com/tq?key=" + SHEETS_KEY);


    }


    private ArrayList<SheetsRating> processJson(JSONObject object) {

        ArrayList<SheetsRating> ratings = new ArrayList<>();

        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String name = columns.getJSONObject(1).getString("v");
                String vk_id = columns.getJSONObject(2).getString("v");
                int total = columns.getJSONObject(8).getInt("v");
                if(name.equals("") || !vk_id.equals("")){
                    SheetsRating rating = new SheetsRating(name, vk_id, total);
                    ratings.add(rating);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ratings;
    }

}
