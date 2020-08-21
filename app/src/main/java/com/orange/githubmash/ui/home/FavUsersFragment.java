package com.orange.githubmash.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.orange.githubmash.R;
import com.orange.githubmash.ItemClickSupport;
import com.orange.githubmash.ItemClickSupport.OnItemClickListener;
import com.orange.githubmash.ItemClickSupport.OnItemLongClickListener;
import com.orange.githubmash.data.local.User;
import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.FavUsersFragment */
public class FavUsersFragment extends Fragment {
    /* access modifiers changed from: private */
    public static FavUsersViewModel favUserViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.favUserViewModel = (FavUsersViewModel) ViewModelProviders.of((Fragment) this).get(FavUsersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fav_users, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.favusersrec);
        final LocalUserListAdapter adapter = new LocalUserListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.favUserViewModel.getMyUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                FavUsersFragment.this.favUserViewModel.delete(adapter.getUserAtPosition(position));
                return false;
            }
        }).setOnItemClickListener(new OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FavUsersFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(adapter.getUserAtPosition(position).getUrl())));
            }
        });
        return root;
    }
}