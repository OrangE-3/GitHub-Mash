package com.orange.githubmash;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.data.local.LocalOwner;
import com.orange.githubmash.ui.home.FavGitRepoFragment;
import com.orange.githubmash.ui.home.FavOwnersFragment;
import com.orange.githubmash.ui.home.MyGitRepoFragment;
import com.orange.githubmash.ui.repsearch.SearchRemoteGitRepo;
import com.orange.githubmash.ui.settings.Settings;
import com.orange.githubmash.ui.ownersearch.SearchRemoteOwners;
import com.orange.githubmash.utils.fields.GlobalFields;

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

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    FloatingActionButton fab, fabsearchuser, fabsearchrepostory;
    private static final Float translationY = 100f;
    OvershootInterpolator interpolator = new OvershootInterpolator();
    Boolean isMenuOpen = false;
    private static final int NEW_USER_ACTIVITY_REQUEST_CODE = 1;
    private static final int NEW_REP_ACTIVITY_REQUEST_CODE = 2;
    private SharedPreferences mPreferences;
    private TextView g;
    private TextView h;
    private ImageView v;
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
       mViewModel= ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mPreferences = getSharedPreferences(GlobalFields.sharedPrefFile,MODE_PRIVATE);
       mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        final Context context=this;
        final Activity activity=this;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getTitle().equals("Logout")) {
                    new AlertDialog.Builder(context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Logout?")
                            .setMessage("Are you sure you want to Logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                                    preferencesEditor.putString("TOKEN_NAME", "");
                                    preferencesEditor.putString("TOKEN_TYPE", "");
                                    preferencesEditor.apply();
                                    FavGitRepoFragment.isExecuted=false;
                                    MyGitRepoFragment.isExecuted=false;
                                    FavOwnersFragment.isExecuted=false;
                                    startActivity(new Intent(activity, Login.class));
                                    activity.finish();
                                }

                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                return false;
            }
        });


        g = navigationView.getHeaderView(0).findViewById(R.id.My_Name);
        h = navigationView.getHeaderView(0).findViewById(R.id.My_url);
        v= navigationView.getHeaderView(0).findViewById(R.id.My_Avatar);

        mViewModel.getname().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                g.setText(s);
            }
        });
        mViewModel.geturl().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                h.setText(s);
            }
        });
        mViewModel.getimage().observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                v.setImageBitmap(bitmap);
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
        Intent intent = new Intent(MainActivity.this, SearchRemoteOwners.class);
        startActivityForResult(intent, NEW_USER_ACTIVITY_REQUEST_CODE);

    }

    public void clicksearchr(View view)
    {
        Intent intent = new Intent(MainActivity.this, SearchRemoteGitRepo.class);
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

            FavOwnersFragment.favUserViewModel.insert(new LocalOwner(entry_owner, data.getStringExtra(SearchRemoteOwners.REPL_user), data.getStringExtra(SearchRemoteOwners.REPL_url), data.getStringExtra(SearchRemoteOwners.REPL_avatar)));
                Toast.makeText(getApplicationContext(),"User named: "+data.getStringExtra(SearchRemoteOwners.REPL_user)+" added!",Toast.LENGTH_SHORT).show();
        } else if (requestCode == NEW_REP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            LocalGitRepoModel localGitRepoModel = new LocalGitRepoModel(entry_owner+GlobalFields.localfav, data.getStringExtra(SearchRemoteGitRepo.REPL_owner), data.getStringExtra(SearchRemoteGitRepo.REPL_name), data.getStringExtra(SearchRemoteGitRepo.REPL_description), data.getStringExtra(SearchRemoteGitRepo.REPL_url), data.getIntExtra(SearchRemoteGitRepo.REPL_watchers, 0));
            FavGitRepoFragment.favGitRepoViewModel.insert(localGitRepoModel);
            Toast.makeText(getApplicationContext(),"Repository named: "+data.getStringExtra(SearchRemoteGitRepo.REPL_name)+" added!",Toast.LENGTH_SHORT).show();
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