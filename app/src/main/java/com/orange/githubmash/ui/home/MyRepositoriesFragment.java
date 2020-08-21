package com.orange.githubmash.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orange.githubmash.ItemClickSupport;
import com.orange.githubmash.MainActivity;
import com.orange.githubmash.R;
import com.orange.githubmash.data.Converters.RemoteToLocalRepository;
import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.local.LocalDatabase;
import com.orange.githubmash.data.remote.RemoteRepoModel;
import com.orange.githubmash.ui.settings.Settings;


import java.util.List;
import java.util.Objects;

import static java.util.Collections.shuffle;

public class MyRepositoriesFragment extends Fragment
{
    SwipeRefreshLayout mSwipeRefreshLayout;
    private MyRepoViewModel mRepoViewModel;
    public MyRepositoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_my_repos, container, false);


        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipetorefresh);

        final RecyclerView recyclerView = root.findViewById(R.id.myrepolist);
        //final RepoListAdapter adapter = new RepoListAdapter(getActivity());
        final LocalRepoListAdapter localadapter = new LocalRepoListAdapter(getActivity());
        recyclerView.setAdapter(localadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        final Fragment f=this;

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String u= localadapter.getRepAtPosition(position).getUrl();
                        StringBuilder sb = new StringBuilder();
                        sb.append(u);
                        MyRepositoriesFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                    }
                });

        mRepoViewModel = ViewModelProviders.of(this).get(MyRepoViewModel.class);
        mRepoViewModel.getMyReposfromlocal().observe(getViewLifecycleOwner(), new Observer<List<GitHubRepository> >() {
            @Override
            public void onChanged(@Nullable final List<GitHubRepository>reps) {
                localadapter.setReps(reps);
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                if (networkInfo != null && networkInfo.isConnected()) {
                    // fetch data
                    mRepoViewModel.getMyRepsremote().observe(getViewLifecycleOwner(), new Observer<List<RemoteRepoModel>>() {
                        @Override
                        public void onChanged(List<RemoteRepoModel> remoteRepoModels) {
                            if(remoteRepoModels!=null) {
                                RemoteToLocalRepository converter = new RemoteToLocalRepository(requireContext());
                                for (int i = 0; i < remoteRepoModels.size(); ++i) {
                                    GitHubRepository g = converter.LocalRep(remoteRepoModels.get(i));
                                    mRepoViewModel.insert(g);
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(),"To view your personal Repositories, You must be connected to the internet.",Toast.LENGTH_SHORT).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

            return root;
        }
}