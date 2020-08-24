package com.orange.githubmash.ui.ownersearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.orange.githubmash.utils.ItemClickSupport;
import com.orange.githubmash.R;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.ui.adapters.RemoteOwnerListAdapter;

import java.util.List;

public class SearchRemoteOwners extends AppCompatActivity {
    public static final String REPL_avatar = "com.orange.githubmash.ui.usersearch.reply.avatar";
    public static final String REPL_url = "com.orange.githubmash.ui.usersearch.reply.url";
    public static final String REPL_user = "com.orange.githubmash.ui.usersearch.reply.user";
    SearchRemoteOwnersViewModel searchRemoteOwnersViewModel;
    SearchView textv;
    Toast toast=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_usres);

        RecyclerView recyclerView =findViewById(R.id.usersearchrec);
        final RemoteOwnerListAdapter adapter = new RemoteOwnerListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        textv=(SearchView) findViewById(R.id.usersearchbut);
        searchRemoteOwnersViewModel = ViewModelProviders.of(this).get(SearchRemoteOwnersViewModel.class);
        final LifecycleOwner o=this;
        final Context c=this;
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        textv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                    searchRemoteOwnersViewModel.searchusers(query).observe(o, new Observer<List<RemoteOwner>>() {
                        @Override
                        public void onChanged(List<RemoteOwner> remoteOwners) {
                            adapter.setUsers(remoteOwners);
                        }
                    });
                } else {
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
                RemoteOwner user = adapter.getUserAtPosition(position);
                replyintent.putExtra(SearchRemoteOwners.REPL_user, user.getLogin());
                replyintent.putExtra(SearchRemoteOwners.REPL_url, user.getHtmlUrl());
                replyintent.putExtra(SearchRemoteOwners.REPL_avatar, user.getAvatarUrl());
                SearchRemoteOwners.this.setResult(-1, replyintent);
                SearchRemoteOwners.this.finish();
                return false;
            }
        }).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                SearchRemoteOwners.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(adapter.getUserAtPosition(position).getHtmlUrl())));
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        toast.cancel();
    }
}