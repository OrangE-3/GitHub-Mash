package com.orange.githubmash.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.LocalOwner;
import com.orange.githubmash.ui.settings.Settings;
import com.squareup.picasso.Picasso;

import java.util.List;

/* renamed from: com.orange.githubmash.ui.adapters.LocalUserListAdapter */
public class LocalOwnerListAdapter extends Adapter<LocalOwnerListAdapter.UserViewHolder> {
    private final LayoutInflater mInflater;
    private List<LocalOwner> mOwners;
    private SharedPreferences appsettings;
    private Boolean show_url ;
    private Boolean show_avatar ;
    /* renamed from: com.orange.githubmash.ui.adapters.LocalUserListAdapter$UserViewHolder */
    class UserViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public final ImageView avatarurl;
        /* access modifiers changed from: private */
        public final TextView url;
        /* access modifiers changed from: private */
        public final TextView userName;

        private UserViewHolder(View itemView) {
            super(itemView);
            this.userName = (TextView) itemView.findViewById(R.id.login_text);
            this.url = (TextView) itemView.findViewById(R.id.url_text);
            this.avatarurl = (ImageView) itemView.findViewById(R.id.avatar_url);
        }
    }

    public LocalOwnerListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        appsettings= PreferenceManager.getDefaultSharedPreferences(context);
        show_url = appsettings.getBoolean(Settings.user_urrl,false);
        show_avatar = appsettings.getBoolean(Settings.user_avatar, false);
    }

    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewHolder(this.mInflater.inflate(R.layout.myuseritem, parent, false));
    }

    public void onBindViewHolder(UserViewHolder holder, int position) {
        List<LocalOwner> list = this.mOwners;
        if (list != null) {
            LocalOwner current = (LocalOwner) list.get(position);
            holder.userName.setText(current.getLogin());
            holder.url.setText(current.getUrl());
            holder.avatarurl.setContentDescription(current.getAvatar_url());
            Picasso.get().load(current.getAvatar_url()).resize(200,200).into(holder.avatarurl);

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
        holder.avatarurl.setContentDescription(str);
    }

    public LocalOwner getUserAtPosition(int position) {
        return (LocalOwner) this.mOwners.get(position);
    }

    public void setUsers(List<LocalOwner> reps) {
        this.mOwners = reps;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<LocalOwner> list = this.mOwners;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}