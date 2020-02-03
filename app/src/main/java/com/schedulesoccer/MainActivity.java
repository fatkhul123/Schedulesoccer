package com.schedulesoccer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.schedulesoccer.Login.SHARED_PREFS;
import static com.schedulesoccer.Login.USERNAME;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rvSurah;
    private jadwalAdapter allLeaguesAdapter;
    private List<ModelJadwal> allLeagueList = new ArrayList<>();
    private ProgressDialog mProgress;


    SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvSurah = findViewById(R.id.recycler_view);
        swipeLayout = findViewById(R.id.swipe_container);
        TextView tuser = findViewById(R.id.textViewUser);
        ImageView profile = findViewById(R.id.userpict);


//        mProgress = new ProgressDialog(this);
//        mProgress.setTitle("Processing...");
//        mProgress.setMessage("Please wait...");
//        mProgress.setCancelable(false);
//        mProgress.setIndeterminate(true);
//
//        mProgress.show();


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code here
                allLeagueList.clear();
                fetchscheduleApi();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 1000);
                Toast.makeText(getApplicationContext(), "DATA SIAP", Toast.LENGTH_SHORT).show();// Delay in millis
            }
        });
        setupRecycler();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in =  new Intent(MainActivity.this, User.class);
                startActivity(in);
            }
        });

        fetchscheduleApi();

    }

    private void setupRecycler() {
        allLeaguesAdapter = new jadwalAdapter(this, allLeagueList);
        rvSurah.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvSurah.setHasFixedSize(true);
        rvSurah.setAdapter(allLeaguesAdapter);
    }

    private void fetchscheduleApi() {
        AndroidNetworking.get(Constants.BASE_URL)
                .setTag("leagues")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray hasilList = response.getJSONArray("events");
                            for (int i = 0; i < hasilList.length(); i++) {
                                JSONObject hasil = hasilList.getJSONObject(i);
                                ModelJadwal item = new ModelJadwal();
                                item.setStrHomeTeam(hasil.getString("strHomeTeam"));
                                item.setStrAwayTeam(hasil.getString("strAwayTeam"));
                                item.setStrDate(hasil.getString("strDate"));
                                item.setStrTime(hasil.getString("strTime"));
                                item.setIntAwayScore(hasil.getString("intAwayScore"));
                                item.setIntHomeScore(hasil.getString("intHomeScore"));
                                item.setStrEvent(hasil.getString("strEvent"));
                                item.setStrHomeforward(hasil.getString("strHomeLineupForward"));
                                item.setStrAwayforward(hasil.getString("strAwayLineupForward"));
                                item.setStrHomeLineupGoalkeeper(hasil.getString("strHomeLineupGoalkeeper"));
                                item.setStrAwayLineupGoalkeeper(hasil.getString("strAwayLineupGoalkeeper"));
                                item.setStrAwayLineupMidfield(hasil.getString("strAwayLineupMidfield"));
                                item.setStrHomeLineupMidfield(hasil.getString("strHomeLineupMidfield"));
                                item.setStrAwayLineupDefense(hasil.getString("strAwayLineupDefense"));
                                item.setStrHomeLineupDefense(hasil.getString("strHomeLineupDefense"));
                                item.setStrThumb(hasil.getString("strThumb"));
                                System.out.println("qwert " + hasil.getString("strEvent"));
                                allLeagueList.add(item);
                            }

                            allLeaguesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("", "onError: " + anError.getErrorBody());
                        Toast.makeText(getApplicationContext(), Constants.EROR, Toast.LENGTH_SHORT).show();
                    }
                });


    }



    @Override
    public void onBackPressed() {

        moveTaskToBack(true);
    }

}

