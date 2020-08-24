package com.orange.githubmash.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.orange.githubmash.ui.home.FavGitRepoFragment;
import com.orange.githubmash.ui.home.FavOwnersFragment;
import com.orange.githubmash.ui.home.MyGitRepoFragment;

public class GitRepoTabsPagerAdapter extends FragmentStatePagerAdapter
{
    int notabs;
    public GitRepoTabsPagerAdapter(@NonNull FragmentManager fm, int tabs) {
        super(fm);
        this.notabs=tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

       if(position==0)return new MyGitRepoFragment();
        if(position==1)return new FavGitRepoFragment();
        if(position==2)return new FavOwnersFragment();

        return null;
    }

    @Override
    public int getCount() {
        return notabs;
    }
}
