package com.orange.githubmash.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.orange.githubmash.MainActivity;
import com.orange.githubmash.R;
import com.orange.githubmash.data.remote.RemoteRepoModel;
import com.orange.githubmash.ui.settings.Settings;

import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.RepoListAdapter */
public class RepoListAdapter extends Adapter<RepoListAdapter.RepoViewHolder> {
    private final LayoutInflater mInflater;
    private List<RemoteRepoModel> mRepos;
    private SharedPreferences appsettings;
    public Integer iscreated=0;
    /* renamed from: com.orange.githubmash.ui.home.RepoListAdapter$RepoViewHolder */
    class RepoViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public final TextView repoDescription;
        /* access modifiers changed from: private */
        public final TextView repoName;
        /* access modifiers changed from: private */
        public final TextView repoOwner;
        /* access modifiers changed from: private */
        public final TextView repoWatchers;

        private RepoViewHolder(View itemView) {
            super(itemView);
            this.repoName = (TextView) itemView.findViewById(R.id.myrepname);
            this.repoOwner = (TextView) itemView.findViewById(R.id.Owner);
            this.repoDescription = (TextView) itemView.findViewById(R.id.created);
            this.repoWatchers = (TextView) itemView.findViewById(R.id.Updated);
        }
    }

    public RepoListAdapter(Context context) {
        iscreated=1;
        this.mInflater = LayoutInflater.from(context);
        appsettings= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepoViewHolder(this.mInflater.inflate(R.layout.myrepitem, parent, false));
    }

    public void onBindViewHolder(RepoViewHolder holder, int position) {
        List<RemoteRepoModel> list = this.mRepos;
        if (list != null) {
            RemoteRepoModel current = (RemoteRepoModel) list.get(position);
            holder.repoName.setText(current.getmName());
            holder.repoOwner.setText("Owner: "+current.getOwner().getLogin());
            holder.repoDescription.setText("Description: "+current.getmDescription());
            holder.repoWatchers.setText("Number of Watchers: "+String.valueOf(current.getmWatchers()));
            return;
        }
        String str = "NA";
        holder.repoName.setText(str);
        holder.repoOwner.setText(str);
        holder.repoDescription.setText(str);
        holder.repoWatchers.setText(str);
    }

    public RemoteRepoModel getRepAtPosition(int position) {
        return (RemoteRepoModel) this.mRepos.get(position);
    }

    public void setReps(List<RemoteRepoModel> reps) {
        this.mRepos = reps;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<RemoteRepoModel> list = this.mRepos;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}