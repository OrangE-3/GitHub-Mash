package com.orange.githubmash.ui.usersearch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.orange.githubmash.ui.home.MyRepoViewModel;
import com.orange.githubmash.ui.home.RepoListAdapter;
import com.orange.githubmash.ui.home.UserListAdapter;
import com.orange.githubmash.ui.repsearch.SearchRepViewModel;

import java.util.List;

public class SearchUsres extends AppCompatActivity {
    public static final String REPL_avatar = "com.orange.githubmash.ui.usersearch.reply.avatar";
    public static final String REPL_url = "com.orange.githubmash.ui.usersearch.reply.url";
    public static final String REPL_user = "com.orange.githubmash.ui.usersearch.reply.user";
    SearchUsersViewModel searchUsersViewModel;
    EditText text;
    Toast toast=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_usres);

        RecyclerView recyclerView =findViewById(R.id.usersearchrec);
        final UserListAdapter adapter = new UserListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        text=(EditText)findViewById(R.id.usersearchbut);
        searchUsersViewModel= ViewModelProviders.of(this).get(SearchUsersViewModel.class);
        final LifecycleOwner o=this;
        final Context c=this;
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        text.addTextChangedListener(new TextWatcher() {
            CountDownTimer timer = null;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
                        String query = editable.toString();
                        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            // fetch data
                            searchUsersViewModel.searchusers(query).observe(o, new Observer<List<RemoteOwner>>() {
                                @Override
                                public void onChanged(List<RemoteOwner> remoteOwners) {
                                    adapter.setUsers(remoteOwners);
                                }
                            });
                        } else {
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
                RemoteOwner user = adapter.getUserAtPosition(position);
                replyintent.putExtra(SearchUsres.REPL_user, user.getLogin());
                replyintent.putExtra(SearchUsres.REPL_url, user.getHtmlUrl());
                replyintent.putExtra(SearchUsres.REPL_avatar, user.getAvatarUrl());
                SearchUsres.this.setResult(-1, replyintent);
                SearchUsres.this.finish();
                return false;
            }
        }).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                SearchUsres.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(adapter.getUserAtPosition(position).getHtmlUrl())));
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        toast.cancel();
    }
}