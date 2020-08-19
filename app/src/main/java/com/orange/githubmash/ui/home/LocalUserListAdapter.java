package com.orange.githubmash.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.User;
import com.orange.githubmash.ui.settings.Settings;

import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.LocalUserListAdapter */
public class LocalUserListAdapter extends Adapter<LocalUserListAdapter.UserViewHolder> {
    private final LayoutInflater mInflater;
    private List<User> mOwners;
    private SharedPreferences appsettings;
    private Boolean show_url ;
    private Boolean show_avatar ;
    /* renamed from: com.orange.githubmash.ui.home.LocalUserListAdapter$UserViewHolder */
    class UserViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public final TextView avatarurl;
        /* access modifiers changed from: private */
        public final TextView url;
        /* access modifiers changed from: private */
        public final TextView userName;

        private UserViewHolder(View itemView) {
            super(itemView);
            this.userName = (TextView) itemView.findViewById(R.id.login_text);
            this.url = (TextView) itemView.findViewById(R.id.url_text);
            this.avatarurl = (TextView) itemView.findViewById(R.id.avatar_url);
        }
    }

    public LocalUserListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        appsettings= PreferenceManager.getDefaultSharedPreferences(context);
        show_url = appsettings.getBoolean(Settings.user_urrl,false);
        show_avatar = appsettings.getBoolean(Settings.user_avatar, false);
    }

    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(this.mInflater.inflate(R.layout.myuseritem, parent, false));
    }

    public void onBindViewHolder(UserViewHolder holder, int position) {
        List<User> list = this.mOwners;
        if (list != null) {
            User current = (User) list.get(position);
            holder.userName.setText(current.getLogin());
            holder.url.setText(current.getUrl());
            holder.avatarurl.setText(current.getAvatar_url());

            if(!show_url)
            {
                holder.url.setVisibility(View.GONE);
            }
            if(!show_avatar)
            {
                holder.avatarurl.setVisibility(View.GONE);
            }
            return;
        }
        String str = "NA";
        holder.userName.setText(str);
        holder.url.setText(str);
        holder.avatarurl.setText(str);
    }

    public User getUserAtPosition(int position) {
        return (User) this.mOwners.get(position);
    }

    public void setUsers(List<User> reps) {
        this.mOwners = reps;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<User> list = this.mOwners;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}