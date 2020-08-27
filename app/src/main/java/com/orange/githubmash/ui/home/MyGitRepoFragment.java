package com.orange.githubmash.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orange.githubmash.ui.adapters.LocalGitRepoListAdapter;
import com.orange.githubmash.utils.ItemClickSupport;
import com.orange.githubmash.R;
import com.orange.githubmash.data.Converters.RemoteToLocalConverter;
import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;


import java.util.List;

import static java.util.Collections.shuffle;

public class MyGitRepoFragment extends Fragment
{
    SwipeRefreshLayout mSwipeRefreshLayout;
    private MyGitRepoViewModel mRepoViewModel;
    private NetworkInfo networkInfo;
    public static Boolean isExecuted=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_my_repos, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipetorefresh);
        mRepoViewModel = ViewModelProviders.of(this).get(MyGitRepoViewModel.class);
        final ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        if(!isExecuted && networkInfo != null && networkInfo.isConnected())initialise();
        final RecyclerView recyclerView = root.findViewById(R.id.myrepolist);
        //final RepoListAdapter adapter = new RepoListAdapter(getActivity());
        final LocalGitRepoListAdapter localadapter = new LocalGitRepoListAdapter(getActivity(),this.getClass());
        recyclerView.setAdapter(localadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        final Fragment f=this;

        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        String u= localadapter.getRepAtPosition(position).getUrl();
                        StringBuilder sb = new StringBuilder();
                        sb.append(u);
                        MyGitRepoFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
                    }
                });

        mRepoViewModel.getMyReposfromlocal().observe(getViewLifecycleOwner(), new Observer<List<LocalGitRepoModel> >() {
            @Override
            public void onChanged(List<LocalGitRepoModel>reps) {
                localadapter.setReps(reps);
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    mRepoViewModel.deleteAll();
                    // fetch data
                    mRepoViewModel.getMyRepsremote().observe(getViewLifecycleOwner(), new Observer<List<RemoteGitRepoModel>>() {
                        @Override
                        public void onChanged(List<RemoteGitRepoModel> remoteGitRepoModels) {
                            if(remoteGitRepoModels !=null) {
                                RemoteToLocalConverter converter = new RemoteToLocalConverter(requireContext());
                                for (int i = 0; i < remoteGitRepoModels.size(); ++i) {
                                    LocalGitRepoModel g = converter.LocalRep(remoteGitRepoModels.get(i),i);
                                    mRepoViewModel.insert(g);
                                }
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(),"To Refresh your personal Repositories, You must be connected to the internet.",Toast.LENGTH_SHORT).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });

            return root;
        }

        private void initialise()
        {
            isExecuted=true;
            mRepoViewModel.deleteAll();
                mRepoViewModel.getMyRepsremote().observe(getViewLifecycleOwner(), new Observer<List<RemoteGitRepoModel>>() {
                    @Override
                    public void onChanged(List<RemoteGitRepoModel> remoteGitRepoModels) {
                        if(remoteGitRepoModels !=null) {
                            RemoteToLocalConverter converter = new RemoteToLocalConverter(requireContext());
                            for (int i = 0; i < remoteGitRepoModels.size(); ++i) {
                                LocalGitRepoModel g = converter.LocalRep(remoteGitRepoModels.get(i),i);
                                mRepoViewModel.insert(g);
                            }
                        }
                    }
                });
        }
}