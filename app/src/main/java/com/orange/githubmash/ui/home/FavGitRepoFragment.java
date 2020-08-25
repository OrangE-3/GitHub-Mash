package com.orange.githubmash.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.orange.githubmash.data.Converters.RemoteToLocalConverter;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;
import com.orange.githubmash.ui.adapters.LocalGitRepoListAdapter;
import com.orange.githubmash.utils.ItemClickSupport;
import com.orange.githubmash.utils.ItemClickSupport.OnItemClickListener;
import com.orange.githubmash.utils.ItemClickSupport.OnItemLongClickListener;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.utils.fields.GlobalFields;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FavGitRepoFragment extends Fragment {
    public static FavGitRepoViewModel favGitRepoViewModel;
    private LocalGitRepoListAdapter adapter;
    private SharedPreferences mPreferences;
    public static Boolean isExecuted=false;
    private NetworkInfo networkInfo;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPreferences = getActivity().getSharedPreferences(GlobalFields.sharedPrefFile, MODE_PRIVATE);
        favGitRepoViewModel = (FavGitRepoViewModel) ViewModelProviders.of(this).get(FavGitRepoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fav_repos, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.favswipe);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.favreprec);
        adapter = new LocalGitRepoListAdapter(getActivity(),this.getClass());
        recyclerView.setAdapter(adapter);
        final ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        networkInfo = connMgr.getActiveNetworkInfo();
        if(!isExecuted && networkInfo != null && networkInfo.isConnected())initialise();

        favGitRepoViewModel.getMyRepos().observe(getViewLifecycleOwner(), new Observer<List<LocalGitRepoModel>>() {
            public void onChanged(List<LocalGitRepoModel> reps) {
                adapter.setReps(reps);
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    favGitRepoViewModel.delete(adapter.getRepAtPosition(position));
                }
                else
                {
                    Toast.makeText(getContext(),"You can't perform this operation without Internet",Toast.LENGTH_SHORT).show();
                }
                    return false;
            }
        }).setOnItemClickListener(new OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String u = adapter.getRepAtPosition(position).getUrl();
                StringBuilder sb = new StringBuilder();
                sb.append(u);
                FavGitRepoFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    favGitRepoViewModel.deleteAll();

                    // fetch data
                    favGitRepoViewModel.getFavRepsremote().observe(getViewLifecycleOwner(), new Observer<List<RemoteGitRepoModel>>() {
                        @Override
                        public void onChanged(List<RemoteGitRepoModel> remoteGitRepoModels) {
                            if(remoteGitRepoModels !=null) {
                                RemoteToLocalConverter converter = new RemoteToLocalConverter(requireContext());
                                for (int i = 0; i < remoteGitRepoModels.size(); ++i) {
                                    LocalGitRepoModel g = converter.LocalRepfav(remoteGitRepoModels.get(i));
                                    favGitRepoViewModel.insert(g);
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(),"To Refresh your favorite Repositories, You must be connected to the internet.",Toast.LENGTH_SHORT).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    private void initialise()
    {
        isExecuted=true;
        favGitRepoViewModel.deleteAll();

        favGitRepoViewModel.getFavRepsremote().observe(getViewLifecycleOwner(), new Observer<List<RemoteGitRepoModel>>() {
            @Override
            public void onChanged(List<RemoteGitRepoModel> remoteGitRepoModels) {
                if(remoteGitRepoModels !=null) {
                    RemoteToLocalConverter converter = new RemoteToLocalConverter(requireContext());
                    for (int i = 0; i < remoteGitRepoModels.size(); ++i) {
                        LocalGitRepoModel g = converter.LocalRepfav(remoteGitRepoModels.get(i));
                        favGitRepoViewModel.insert(g);
                    }
                }
            }
        });
    }
}