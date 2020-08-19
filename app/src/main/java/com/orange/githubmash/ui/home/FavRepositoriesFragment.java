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
import com.orange.githubmash.ItemClickSupport;
import com.orange.githubmash.ItemClickSupport.OnItemClickListener;
import com.orange.githubmash.ItemClickSupport.OnItemLongClickListener;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.GitHubRepository;
import java.util.List;

public class FavRepositoriesFragment extends Fragment {
    public FavRepoViewModel favRepoViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.favRepoViewModel = (FavRepoViewModel) ViewModelProviders.of((Fragment) this).get(FavRepoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fav_repos, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.favreprec);
        final LocalRepoListAdapter adapter = new LocalRepoListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.favRepoViewModel.getMyRepos().observe(getViewLifecycleOwner(), new Observer<List<GitHubRepository>>() {
            public void onChanged(List<GitHubRepository> reps) {
                adapter.setReps(reps);
            }
        });
        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                FavRepositoriesFragment.this.favRepoViewModel.delete(adapter.getRepAtPosition(position));
                return false;
            }
        }).setOnItemClickListener(new OnItemClickListener() {
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                String u = adapter.getRepAtPosition(position).getUrl();
                StringBuilder sb = new StringBuilder();
                sb.append(u);
                FavRepositoriesFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb.toString())));
            }
        });
        return root;
    }
}