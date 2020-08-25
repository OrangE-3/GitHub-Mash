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

import com.orange.githubmash.R;
import com.orange.githubmash.data.Converters.RemoteToLocalConverter;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.ui.adapters.LocalOwnerListAdapter;
import com.orange.githubmash.utils.ItemClickSupport;
import com.orange.githubmash.utils.ItemClickSupport.OnItemClickListener;
import com.orange.githubmash.utils.ItemClickSupport.OnItemLongClickListener;
import com.orange.githubmash.data.local.LocalOwner;

import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.FavUsersFragment */
public class FavOwnersFragment extends Fragment {
    /* access modifiers changed from: private */
    public static FavOwnersViewModel favUserViewModel;
    public static Boolean isExecuted=false;
    private NetworkInfo networkInfo;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private LocalOwnerListAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.favUserViewModel = (FavOwnersViewModel) ViewModelProviders.of((Fragment) this).get(FavOwnersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fav_users, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.favswipeuser);
        final ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.favusersrec);
        adapter = new LocalOwnerListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        networkInfo = connMgr.getActiveNetworkInfo();
        if(!isExecuted && networkInfo != null && networkInfo.isConnected())initialise();


        favUserViewModel.getMyUsers().observe(getViewLifecycleOwner(), new Observer<List<LocalOwner>>() {
            public void onChanged(List<LocalOwner> localOwners) {
                adapter.setUsers(localOwners);
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    favUserViewModel.delete(adapter.getUserAtPosition(position));
                }
                else
                {
                    Toast.makeText(getContext(),"You can't perform this operation without Internet",Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }).setOnItemClickListener(new OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FavOwnersFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(adapter.getUserAtPosition(position).getUrl())));
            }
        });


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    favUserViewModel.deleteAll();

                    favUserViewModel.getFavOwnersRemote().observe(getViewLifecycleOwner(), new Observer<List<RemoteOwner>>() {

                        @Override
                        public void onChanged(List<RemoteOwner> remoteOwners) {
                            if(remoteOwners !=null) {
                                RemoteToLocalConverter converter = new RemoteToLocalConverter(requireContext());
                                for (int i = 0; i < remoteOwners.size(); ++i) {
                                    LocalOwner g = converter.LocalOwnerfav(remoteOwners.get(i));
                                    favUserViewModel.insert(g);
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(),"To Refresh your favorite Users, You must be connected to the internet.",Toast.LENGTH_SHORT).show();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


        return root;
    }

    private void initialise()
    {
        isExecuted=true;
        favUserViewModel.deleteAll();

        favUserViewModel.getFavOwnersRemote().observe(getViewLifecycleOwner(), new Observer<List<RemoteOwner>>() {

            @Override
            public void onChanged(List<RemoteOwner> remoteOwners) {
                if(remoteOwners !=null) {
                    RemoteToLocalConverter converter = new RemoteToLocalConverter(requireContext());
                    for (int i = 0; i < remoteOwners.size(); ++i) {
                        LocalOwner g = converter.LocalOwnerfav(remoteOwners.get(i));
                        favUserViewModel.insert(g);
                    }
                }
            }
        });
    }
}