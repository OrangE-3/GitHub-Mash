package com.orange.githubmash.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class RepoTabsPagerAdapter extends FragmentStatePagerAdapter
{
    int notabs;
    public RepoTabsPagerAdapter(@NonNull FragmentManager fm, int tabs) {
        super(fm);
        this.notabs=tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

       if(position==0)return new MyRepositoriesFragment();
        if(position==1)return new FavRepositoriesFragment();
        if(position==2)return new FavUsersFragment();

        return null;
    }

    @Override
    public int getCount() {
        return notabs;
    }
}
