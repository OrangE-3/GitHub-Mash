package com.orange.githubmash.ui.repsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.orange.githubmash.ItemClickSupport;
import com.orange.githubmash.R;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.data.remote.RemoteRepoModel;
import com.orange.githubmash.ui.home.RepoListAdapter;

import java.util.List;

public class SearchRepositories extends AppCompatActivity {
    public static final String REPL_description = "com.orange.githubmash.ui.repsearch.reply.description";
    public static final String REPL_name = "com.orange.githubmash.ui.repsearch.reply.name";
    public static final String REPL_owner = "com.orange.githubmash.ui.repsearch.reply.owner";
    public static final String REPL_url = "com.orange.githubmash.ui.repsearch.reply.url";
    public static final String REPL_watchers = "com.orange.githubmash.ui.repsearch.reply.watchers";
    Toast toast=null;
    SearchRepViewModel searchRepViewModel;
    EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_repositories);

        RecyclerView recyclerView =findViewById(R.id.repsearchrec);
        final RepoListAdapter adapter = new RepoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        text=(EditText)findViewById(R.id.repsearchbut);
        searchRepViewModel= ViewModelProviders.of(this).get(SearchRepViewModel.class);
        final LifecycleOwner o=this;
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        text.addTextChangedListener(new TextWatcher() {
            CountDownTimer timer = null;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if (timer != null) {
                    timer.cancel();
                }
                timer = new CountDownTimer(300, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {

                        //do what you wish
                        String query= editable.toString();

                        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            // fetch data
                            searchRepViewModel.searchrep(query).observe(o, new Observer<List<RemoteRepoModel>>() {
                                @Override
                                public void onChanged(List<RemoteRepoModel> remoteRepoModels)
                                {

                                    adapter.setReps(remoteRepoModels);
                                }
                            });
                        }
                        else {
                            toast.setText("You must be connected to the internet.");
                            toast.show();
                        }

                    }

                }.start();
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                Intent replyintent = new Intent();
                RemoteRepoModel rep = adapter.getRepAtPosition(position);
                replyintent.putExtra(SearchRepositories.REPL_name, rep.getmName());
                replyintent.putExtra(SearchRepositories.REPL_owner, rep.getOwner().getLogin());
                replyintent.putExtra(SearchRepositories.REPL_description, rep.getmDescription());
                replyintent.putExtra(SearchRepositories.REPL_url, rep.getmUrl());
                replyintent.putExtra(SearchRepositories.REPL_watchers, rep.getmWatchers());
                SearchRepositories.this.setResult(-1, replyintent);
                SearchRepositories.this.finish();
                return false;
            }
        }).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                SearchRepositories.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(adapter.getRepAtPosition(position).getmUrl())));
            }
        });
    }
}