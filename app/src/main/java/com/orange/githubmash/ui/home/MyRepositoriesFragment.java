package com.orange.githubmash.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.githubmash.ItemClickSupport;
import com.orange.githubmash.MainActivity;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.remote.RemoteRepoModel;
import com.orange.githubmash.ui.settings.Settings;


import java.util.List;

public class MyRepositoriesFragment extends Fragment
{

    private MyRepoViewModel mRepoViewModel;
    public MyRepositoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_my_repos, container, false);





        RecyclerView recyclerView = root.findViewById(R.id.myrepolist);
        final RepoListAdapter adapter = new RepoListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            mRepoViewModel = ViewModelProviders.of(this).get(MyRepoViewModel.class);
            mRepoViewModel.getMyRepos().observe(getViewLifecycleOwner(), new Observer<List<RemoteRepoModel> >() {
                @Override
                public void onChanged(@Nullable final List<RemoteRepoModel>reps) {
                    // Update the cached copy of the words in the adapter.
                    adapter.setReps(reps);
                }
            });
        } else {
            Toast.makeText(getContext(),"To view your personal Repositories, You must be connected to the internet.",Toast.LENGTH_SHORT).show();
        }


        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String u = adapter.getRepAtPosition(position).getmUrl();
                        StringBuilder sb = new StringBuilder();
                        sb.append(u);
                        MyRepositoriesFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                    }
                });


            return root;
        }
}