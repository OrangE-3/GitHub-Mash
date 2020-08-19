package com.orange.githubmash.ui.usersearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.orange.githubmash.ItemClickSupport;
import com.orange.githubmash.R;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.data.remote.RemoteRepoModel;
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

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query= charSequence.toString();
                searchUsersViewModel.searchusers(query).observe(o, new Observer<List<RemoteOwner>>() {
                    @Override
                    public void onChanged(List<RemoteOwner> remoteOwners)
                    {
                        adapter.setUsers(remoteOwners);
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
}