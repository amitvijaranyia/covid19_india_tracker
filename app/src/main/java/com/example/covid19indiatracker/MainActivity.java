package com.example.covid19indiatracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "tag";

    TextView tvTotalConfirmed, tvTotalActive, tvTotalRecovered, tvTotalDeceased, tvSpace;
    TextView tvConfirmedSort, tvActiveSort, tvRecoveredSort, tvDeceasedSort;
    ImageView ivConfirmed, ivActive, ivRecovered, ivDeceased;
    RecyclerView rvStateWiseList;
    List<StatewisePojo> stateWiseList;
    StateWiseListAdapter stateWiseListAdapter;
    final String url = "https://api.covid19india.org/data.json";

    SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalConfirmed = findViewById(R.id.tvTotalConfirmed);
        tvTotalActive = findViewById(R.id.tvTotalActive);
        tvTotalRecovered = findViewById(R.id.tvTotalRecovered);
        tvTotalDeceased = findViewById(R.id.tvTotalDead);
        rvStateWiseList = findViewById(R.id.rvStateWiseList);
        tvConfirmedSort = findViewById(R.id.tvConfirmedSort);
        tvActiveSort = findViewById(R.id.tvActiveSort);
        tvRecoveredSort = findViewById(R.id.tvRecoveredSort);
        tvDeceasedSort = findViewById(R.id.tvDeceasedSort);
        ivConfirmed = findViewById(R.id.ivConfirmed);
        ivActive = findViewById(R.id.ivActive);
        ivRecovered = findViewById(R.id.ivRecovered);
        ivDeceased = findViewById(R.id.ivDeceased);
        tvSpace = findViewById(R.id.tvSpace);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        stateWiseList = new ArrayList<>();

        rvStateWiseList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,  false));
        rvStateWiseList.setHasFixedSize(true);
        stateWiseListAdapter = new StateWiseListAdapter(this);
        rvStateWiseList.setAdapter(stateWiseListAdapter);
        rvStateWiseList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        tvTotalConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortConfirmed();
                stateWiseListAdapter.notifyDataSetChanged();
                confirmedSelected();
            }
        });
        tvConfirmedSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortConfirmed();
                stateWiseListAdapter.notifyDataSetChanged();
                confirmedSelected();
            }
        });
        tvTotalActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortActive();
                stateWiseListAdapter.notifyDataSetChanged();
                activeSelected();
            }
        });
        tvActiveSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortActive();
                stateWiseListAdapter.notifyDataSetChanged();
                activeSelected();
            }
        });
        tvTotalRecovered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortRecovered();
                stateWiseListAdapter.notifyDataSetChanged();
                recoveredSelected();
            }
        });
        tvRecoveredSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortRecovered();
                stateWiseListAdapter.notifyDataSetChanged();
                recoveredSelected();
            }
        });
        tvTotalDeceased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortDeceased();
                stateWiseListAdapter.notifyDataSetChanged();
                deceasedSelected();
            }
        });
        tvDeceasedSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortDeceased();
                stateWiseListAdapter.notifyDataSetChanged();
                deceasedSelected();
            }
        });
        fetchResult();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                stateWiseList.clear();
                fetchResult();
            }
        });
    }

    public void fetchResult(){
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request
                .Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(
                                MainActivity.this,
                                "Error while fetching data.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String rawJson = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bindViews(rawJson);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void bindViews(String json) throws JSONException, ParseException {
        swipeRefresh.setRefreshing(false);
        JSONObject rawJsonObject = new JSONObject(json);
        Log.d(TAG, "bindViews: " + "here");
        JSONArray jsonArray = rawJsonObject.getJSONArray("statewise");
        JSONObject firstObject = jsonArray.getJSONObject(0);
        tvTotalConfirmed.setText(firstObject.getString("confirmed"));
        tvTotalActive.setText(firstObject.getString("active"));
        tvTotalRecovered.setText(firstObject.getString("recovered"));
        tvTotalDeceased.setText(firstObject.getString("deaths"));
        String myDate = firstObject.getString("lastupdatedtime");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", new Locale("en"));
        Date past = sdf.parse(myDate);
        String timeAgo = "Last Updated\n"+getTimeAgo(past);
        tvSpace.setText(timeAgo);
        parseJson(jsonArray);
    }

    public void parseJson(JSONArray jsonArray) throws JSONException {
        for(int i = 1; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String stateName = jsonObject.getString("state");
            String confirmed = jsonObject.getString("confirmed");
            String active = jsonObject.getString("active");
            String recovered = jsonObject.getString("recovered");
            String deceased = jsonObject.getString("deaths");
            String confirmedIncrease = jsonObject.getString("deltaconfirmed");
            String deceasedIncrease = jsonObject.getString("deltadeaths");
            String recoveredIncrease = jsonObject.getString("deltarecovered");
            String activeIncrease = String.valueOf(Integer.parseInt(confirmedIncrease)
                    -(Integer.parseInt(recoveredIncrease)+Integer.parseInt(deceasedIncrease)));
            StatewisePojo statewisePojo = new StatewisePojo(
                    stateName,
                    confirmed,
                    active,
                    recovered,
                    deceased,
                    confirmedIncrease,
                    activeIncrease,
                    recoveredIncrease,
                    deceasedIncrease
            );
            stateWiseList.add(statewisePojo);
        }
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String stateName = jsonObject.getString("state");
        String confirmed = jsonObject.getString("confirmed");
        String active = jsonObject.getString("active");
        String recovered = jsonObject.getString("recovered");
        String deceased = jsonObject.getString("deaths");
        String confirmedIncrease = jsonObject.getString("deltaconfirmed");
        String deceasedIncrease = jsonObject.getString("deltadeaths");
        String recoveredIncrease = jsonObject.getString("deltarecovered");
        String activeIncrease = String.valueOf(Integer.parseInt(confirmedIncrease)
                -(Integer.parseInt(recoveredIncrease)+Integer.parseInt(deceasedIncrease)));
        StatewisePojo statewisePojo = new StatewisePojo(
                stateName,
                confirmed,
                active,
                recovered,
                deceased,
                confirmedIncrease,
                activeIncrease,
                recoveredIncrease,
                deceasedIncrease
        );
        stateWiseList.add(statewisePojo);
        sortConfirmed();
        stateWiseListAdapter.setStateWiseList(stateWiseList);
        confirmedSelected();
    }
    public void sortConfirmed(){
        StatewisePojo pojo = stateWiseList.remove(stateWiseList.size()-1);
        Collections.sort(stateWiseList, new Comparator<StatewisePojo>() {
            @Override
            public int compare(StatewisePojo o1, StatewisePojo o2) {
                int num1 = Integer.parseInt(o1.confirmed);
                int num2 = Integer.parseInt(o2.confirmed);
                if(num1 > num2) return -1;
                else return 1;
            }
        });
        stateWiseList.add(pojo);
    }
    public void sortActive(){
        StatewisePojo pojo = stateWiseList.remove(stateWiseList.size()-1);
        Collections.sort(stateWiseList, new Comparator<StatewisePojo>() {
            @Override
            public int compare(StatewisePojo o1, StatewisePojo o2) {
                int num1 = Integer.parseInt(o1.active);
                int num2 = Integer.parseInt(o2.active);
                if(num1 > num2) return -1;
                else return 1;
            }
        });
        stateWiseList.add(pojo);
    }
    public void sortRecovered(){
        StatewisePojo pojo = stateWiseList.remove(stateWiseList.size()-1);
        Collections.sort(stateWiseList, new Comparator<StatewisePojo>() {
            @Override
            public int compare(StatewisePojo o1, StatewisePojo o2) {
                int num1 = Integer.parseInt(o1.recovered);
                int num2 = Integer.parseInt(o2.recovered);
                if(num1 > num2) return -1;
                else return 1;
            }
        });
        stateWiseList.add(pojo);
    }
    public void sortDeceased(){
        StatewisePojo pojo = stateWiseList.remove(stateWiseList.size()-1);
        Collections.sort(stateWiseList, new Comparator<StatewisePojo>() {
            @Override
            public int compare(StatewisePojo o1, StatewisePojo o2) {
                int num1 = Integer.parseInt(o1.deceased);
                int num2 = Integer.parseInt(o2.deceased);
                if(num1 > num2) return -1;
                else return 1;
            }
        });
        stateWiseList.add(pojo);
    }
    public void confirmedSelected(){
        tvSpace.setVisibility(View.VISIBLE);
        ivConfirmed.setVisibility(View.VISIBLE);
        ivActive.setVisibility(View.INVISIBLE);
        ivRecovered.setVisibility(View.INVISIBLE);
        ivDeceased.setVisibility(View.INVISIBLE);
    }
    public void activeSelected(){
        tvSpace.setVisibility(View.VISIBLE);
        ivConfirmed.setVisibility(View.INVISIBLE);
        ivActive.setVisibility(View.VISIBLE);
        ivRecovered.setVisibility(View.INVISIBLE);
        ivDeceased.setVisibility(View.INVISIBLE);
    }
    public void recoveredSelected(){
        tvSpace.setVisibility(View.VISIBLE);
        ivConfirmed.setVisibility(View.INVISIBLE);
        ivActive.setVisibility(View.INVISIBLE);
        ivRecovered.setVisibility(View.VISIBLE);
        ivDeceased.setVisibility(View.INVISIBLE);
    }
    public void deceasedSelected(){
        tvSpace.setVisibility(View.VISIBLE);
        ivConfirmed.setVisibility(View.INVISIBLE);
        ivActive.setVisibility(View.INVISIBLE);
        ivRecovered.setVisibility(View.INVISIBLE);
        ivDeceased.setVisibility(View.VISIBLE);
    }
    public String getTimeAgo(Date past){
        Date current = new Date();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(current.getTime()-past.getTime());
        long minutes = TimeUnit.MINUTES.toMinutes(current.getTime()-past.getTime());
        long hours = TimeUnit.HOURS.toHours(current.getTime()-past.getTime());
        if(seconds < 60){
            return "Few seconds ago";
        }
        else if(minutes < 60){
            return minutes+" minutes ago";
        }
        else if(hours < 24){
            return hours+" hours ago";
        }
        else{
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy, hh:mm a", new Locale("en"));
            return format.format(past);
        }
    }
}
