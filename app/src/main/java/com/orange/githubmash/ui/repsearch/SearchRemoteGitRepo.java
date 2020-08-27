package com.orange.githubmash.ui.repsearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.orange.githubmash.utils.ItemClickSupport;
import com.orange.githubmash.R;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;
import com.orange.githubmash.ui.adapters.RemoteGitRepoListAdapter;

import java.util.List;

public class SearchRemoteGitRepo extends AppCompatActivity {
    public static final String REPL_description = "com.orange.githubmash.ui.repsearch.reply.description";
    public static final String REPL_name = "com.orange.githubmash.ui.repsearch.reply.name";
    public static final String REPL_owner = "com.orange.githubmash.ui.repsearch.reply.owner";
    public static final String REPL_url = "com.orange.githubmash.ui.repsearch.reply.url";
    public static final String REPL_watchers = "com.orange.githubmash.ui.repsearch.reply.watchers";
    Toast toast=null;
    private SearchView textv;
    public static ProgressBar repbar;
    SearchRemoteGitRepoViewModel searchRemoteGitRepoViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_repositories);
        repbar=findViewById(R.id.repbar);
        final RecyclerView recyclerView =findViewById(R.id.repsearchrec);
        final RemoteGitRepoListAdapter adapter = new RemoteGitRepoListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textv= (SearchView) findViewById(R.id.repsearchbut);
        searchRemoteGitRepoViewModel = ViewModelProviders.of(this).get(SearchRemoteGitRepoViewModel.class);
        final LifecycleOwner o=this;
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        textv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                    repbar.setVisibility(View.VISIBLE);
                    searchRemoteGitRepoViewModel.searchrep(query).observe(o, new Observer<List<RemoteGitRepoModel>>() {
                        @Override
                        public void onChanged(List<RemoteGitRepoModel> remoteGitRepoModels)
                        {
                                adapter.setReps(remoteGitRepoModels);
                        }

                    });
                }
                else {
                    toast.setText("You must be connected to the internet.");
                    toast.show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }


        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                Intent replyintent = new Intent();
                RemoteGitRepoModel rep = adapter.getRepAtPosition(position);
                replyintent.putExtra(SearchRemoteGitRepo.REPL_name, rep.getmName());
                replyintent.putExtra(SearchRemoteGitRepo.REPL_owner, rep.getOwner().getLogin());
                replyintent.putExtra(SearchRemoteGitRepo.REPL_description, rep.getmDescription());
                replyintent.putExtra(SearchRemoteGitRepo.REPL_url, rep.getmUrl());
                replyintent.putExtra(SearchRemoteGitRepo.REPL_watchers, rep.getmWatchers());
                setResult(RESULT_OK, replyintent);
                finish();
                return false;
            }
        }).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                SearchRemoteGitRepo.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(adapter.getRepAtPosition(position).getmUrl())));
            }
        });
    }
}