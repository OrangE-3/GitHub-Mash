package com.orange.githubmash;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.local.User;
import com.orange.githubmash.data.remote.AccessToken;
import com.orange.githubmash.data.remote.GitHubClient;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.ui.home.FavRepoViewModel;
import com.orange.githubmash.ui.home.FavUsersFragment;
import com.orange.githubmash.ui.home.FavUsersViewModel;
import com.orange.githubmash.ui.home.MyRepoViewModel;
import com.orange.githubmash.ui.repsearch.SearchRepositories;
import com.orange.githubmash.ui.settings.Settings;
import com.orange.githubmash.ui.usersearch.SearchUsres;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    FloatingActionButton fab, fabsearchuser, fabsearchrepostory;
    Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    private static final String TAG = "MainActivity";
    Boolean isMenuOpen = false;
    public static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;
    public static final int NEW_REP_ACTIVITY_REQUEST_CODE = 2;
    private String sharedPrefFile = "com.example.android.hellosharedprefs";
    private FavRepoViewModel favRepoViewModel;
    private FavUsersViewModel favUsersViewModel;
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFabMenu();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        this.favUsersViewModel = ViewModelProviders.of(this).get(FavUsersViewModel.class);
        this.favRepoViewModel = ViewModelProviders.of(this).get(FavRepoViewModel.class);
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        this.mPreferences = sharedPreferences;
        mViewModel= ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.getme().observe(this, new Observer<RemoteOwner>() {
            @Override
            public void onChanged(RemoteOwner remoteOwner)
            {
                TextView g = findViewById(R.id.My_Name);
                TextView h = findViewById(R.id.My_url);
                String gg = mPreferences.getString("USER_NAME", null);
                String hh = mPreferences.getString("USER_URL", null);
                if(g!=null && h!=null) {
                    g.setText(gg);
                    h.setText(hh);
                }
            }
        });

        View headerView = navigationView.getHeaderView(0);
        TextView g = (TextView) headerView.findViewById(R.id.My_Name);
        TextView h = (TextView) headerView.findViewById(R.id.My_url);
        ImageView v=headerView.findViewById(R.id.My_Avatar);
        String gg = mPreferences.getString("USER_NAME", null);
        String hh = mPreferences.getString("USER_URL", null);
        g.setText(gg);
        h.setText(hh);
        Picasso.get().load(mPreferences.getString("USER_AVATAR", null)).resize(120,120).into(v);
        final Context context=this;
        final Activity activity=this;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout?")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                preferencesEditor.putString("TOKEN_NAME", "");
                                preferencesEditor.putString("TOKEN_TYPE", "");
                                preferencesEditor.apply();
                                startActivity(new Intent(activity, Login.class));
                                activity.finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void initFabMenu() {
        fab = findViewById(R.id.fab);
        fabsearchuser = findViewById(R.id.fabsearchu);
        fabsearchrepostory = findViewById(R.id.fabsearchr);

        fabsearchuser.setAlpha(0f);
        fabsearchrepostory.setAlpha(0f);

        fabsearchuser.setTranslationY(translationY);
        fabsearchrepostory.setTranslationY(translationY);
        fabsearchrepostory.setVisibility(View.INVISIBLE);
        fabsearchuser.setVisibility(View.INVISIBLE);

    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fabsearchrepostory.setVisibility(View.VISIBLE);
        fabsearchuser.setVisibility(View.VISIBLE);
        fab.animate().setInterpolator(interpolator).rotation(180f).setDuration(300).start();

        fabsearchuser.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabsearchrepostory.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();


    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;

        fab.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabsearchuser.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabsearchrepostory.animate().translationY(translationY).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabsearchrepostory.setVisibility(View.INVISIBLE);
        fabsearchuser.setVisibility(View.INVISIBLE);
    }

    public void clicksearchu(View view)
    {
        Intent intent = new Intent(MainActivity.this, SearchUsres.class);
        startActivityForResult(intent, NEW_USER_ACTIVITY_REQUEST_CODE);

    }

    public void clicksearchr(View view)
    {
        Intent intent = new Intent(MainActivity.this, SearchRepositories.class);
        startActivityForResult(intent, NEW_REP_ACTIVITY_REQUEST_CODE);

    }

    public void clickmain(View view)
    {
        if (isMenuOpen)
        {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        String entry_owner = mPreferences.getString("USER_NAME", "");
        if (requestCode == NEW_USER_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            this.favUsersViewModel.insert(new User(entry_owner, data.getStringExtra(SearchUsres.REPL_user), data.getStringExtra(SearchUsres.REPL_url), data.getStringExtra(SearchUsres.REPL_avatar)));
                Toast.makeText(getApplicationContext(),"User named: "+data.getStringExtra(SearchUsres.REPL_user)+" added!",Toast.LENGTH_SHORT).show();
        } else if (requestCode == NEW_REP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            GitHubRepository gitHubRepository = new GitHubRepository(entry_owner, data.getStringExtra(SearchRepositories.REPL_owner), data.getStringExtra(SearchRepositories.REPL_name), data.getStringExtra(SearchRepositories.REPL_description), data.getStringExtra(SearchRepositories.REPL_url), Integer.valueOf(data.getIntExtra(SearchRepositories.REPL_watchers, 0)));
            this.favRepoViewModel.insert(gitHubRepository);

            Toast.makeText(getApplicationContext(),"Repository named: "+data.getStringExtra(SearchRepositories.REPL_name)+" added!",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Nothing Added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings)
        {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}