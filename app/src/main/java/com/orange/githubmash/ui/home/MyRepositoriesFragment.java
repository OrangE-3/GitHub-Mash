package com.orange.githubmash.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.githubmash.MainActivity;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.remote.RemoteRepoModel;
import com.orange.githubmash.ui.settings.SettingsViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRepositoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

        mRepoViewModel = ViewModelProviders.of(this).get(MyRepoViewModel.class);

        mRepoViewModel.getMyRepos().observe(getViewLifecycleOwner(), new Observer<List<RemoteRepoModel> >() {
            @Override
            public void onChanged(@Nullable final List<RemoteRepoModel>reps) {
                // Update the cached copy of the words in the adapter.
                adapter.setReps(reps);
            }
        });


            return root;
        }
}