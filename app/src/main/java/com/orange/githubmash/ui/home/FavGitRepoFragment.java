package com.orange.githubmash.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GlobalFields.sharedPrefFile, MODE_PRIVATE);
        this.mPreferences = sharedPreferences;
        favGitRepoViewModel = (FavGitRepoViewModel) ViewModelProviders.of(this).get(FavGitRepoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fav_repos, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.favreprec);
        adapter = new LocalGitRepoListAdapter(getActivity(),this.getClass());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favGitRepoViewModel.getMyRepos().observe(getViewLifecycleOwner(), new Observer<List<LocalGitRepoModel>>() {
            public void onChanged(List<LocalGitRepoModel> reps) {
                adapter.setReps(reps);
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                favGitRepoViewModel.delete(adapter.getRepAtPosition(position));
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
        return root;
    }
}