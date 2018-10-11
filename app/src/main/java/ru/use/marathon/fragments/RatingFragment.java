package ru.use.marathon.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.use.marathon.R;
import ru.use.marathon.interfaces.SheetsResult;
import ru.use.marathon.models.SheetsRating;
import ru.use.marathon.utils.GetSheetsRating;

/**
 * Created by ilyas on 10-Oct-18.
 */

public class RatingFragment  extends Fragment{

    @BindView(R.id.data_btn)
    Button btn;
    @BindView(R.id.data_txt)
    TextView txt;
    ArrayList<SheetsRating> ratings;

    public RatingFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_rating,container,false);
        ButterKnife.bind(this,view);

        ratings = new ArrayList<>();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetSheetsRating(new SheetsResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        processJson(object);
                        String s = "Data:\n" ;
                        for (SheetsRating rating : ratings){
                            s+=rating.getFullname();
                            s+=" ";
                            s+=rating.getVk_id();
                            s+=" ";
                            s+=rating.getTotal();
                            s+="\n";
                        }
                        txt.setText(s);
                    }
                }).execute("https://spreadsheets.google.com/tq?key=1ZcJFIaj8NxSeGPGvFyohfV76zw08WbxWDGvAskuyxFk");
            }
        });



        return view;
    }


    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String name = columns.getJSONObject(1).getString("v");
                String vk_id = columns.getJSONObject(2).getString("v");
                int total = columns.getJSONObject(8).getInt("v");
                SheetsRating rating = new SheetsRating(name, vk_id, total);
                ratings.add(rating);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
