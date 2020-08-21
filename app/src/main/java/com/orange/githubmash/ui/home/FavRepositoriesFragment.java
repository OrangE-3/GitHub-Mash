package com.orange.githubmash.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.orange.githubmash.ItemClickSupport;
import com.orange.githubmash.ItemClickSupport.OnItemClickListener;
import com.orange.githubmash.ItemClickSupport.OnItemLongClickListener;
import com.orange.githubmash.MainActivity;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.ui.repsearch.SearchRepositories;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.orange.githubmash.MainActivity.NEW_REP_ACTIVITY_REQUEST_CODE;

public class FavRepositoriesFragment extends Fragment {
    public static FavRepoViewModel favRepoViewModel;
    private LocalRepoListAdapter adapter;
    private SharedPreferences mPreferences;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MainActivity.sharedPrefFile, MODE_PRIVATE);
        this.mPreferences = sharedPreferences;
        favRepoViewModel = (FavRepoViewModel) ViewModelProviders.of(this).get(FavRepoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fav_repos, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.favreprec);
        adapter = new LocalRepoListAdapter(getActivity(),this.getClass());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favRepoViewModel.getMyRepos().observe(getViewLifecycleOwner(), new Observer<List<GitHubRepository>>() {
            public void onChanged(List<GitHubRepository> reps) {
                adapter.setReps(reps);
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                favRepoViewModel.delete(adapter.getRepAtPosition(position));
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