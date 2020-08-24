package com.orange.githubmash;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.orange.githubmash.data.local.LocalDatabase;

import com.orange.githubmash.data.local.LocalOwner;
import com.orange.githubmash.data.local.LocalOwnerDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocalOwnerDaoTest 
{
    private LocalOwnerDao localOwnerDao;
    private LocalDatabase db;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();


    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase.class).build();
        localOwnerDao = db.userDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertandreadowners() throws Exception {
        final LocalOwner localOwner= new LocalOwner("foo","bar","go","http://ug.com");
        localOwnerDao.insert(localOwner);
        localOwnerDao.getAllUsers().observeForever(new Observer<List<LocalOwner>>() {
            @Override
            public void onChanged(List<LocalOwner> byName) {
                assertThat(byName.get(0).getLogin(), equalTo(localOwner.getLogin()));
                assertThat(byName.get(0).getOwner(), equalTo(localOwner.getOwner()));
                localOwnerDao.getAllUsers().removeObserver(this);
            }
        });
    }
    @Test
    public void insertandreadmyowners() throws Exception {
        final LocalOwner localOwner1= new LocalOwner("foo","bar","go","http://ug.com");
        final LocalOwner localOwner2= new LocalOwner("bar","foo","go","http://ug.com");
        localOwnerDao.insert(localOwner1);
        localOwnerDao.insert(localOwner2);
        localOwnerDao.getmyUsers("foo").observeForever(new Observer<List<LocalOwner>>() {
            @Override
            public void onChanged(List<LocalOwner> byName) {
                assertThat(byName.get(0).getLogin(), equalTo(localOwner1.getLogin()));
                assertThat(byName.get(0).getOwner(), equalTo(localOwner1.getOwner()));
                localOwnerDao.getmyUsers("foo").removeObserver(this);
            }
        });
    }
    @Test
    public void deleteowner() throws Exception {
        final LocalOwner localOwner1= new LocalOwner("foo","bar","go","http://ug.com");
        final LocalOwner localOwner2= new LocalOwner("bar","foo","go","http://ug.com");
        localOwnerDao.insert(localOwner1);
        localOwnerDao.insert(localOwner2);
        localOwnerDao.deleteuser(localOwner1);
        localOwnerDao.getAllUsers().observeForever(new Observer<List<LocalOwner>>() {
            @Override
            public void onChanged(List<LocalOwner> byName) {
                assertThat(byName.get(0).getOwner(), equalTo(localOwner2.getOwner()));
                assertThat(byName.get(0).getLogin(), equalTo(localOwner2.getLogin()));
                assertThat(byName.size(),equalTo(1));
                localOwnerDao.getAllUsers().removeObserver(this);
            }
        });
    }
    @Test
    public void deleteall() throws Exception {
        final LocalOwner localOwner1= new LocalOwner("foo","bar","go","http://ug.com");
        final LocalOwner localOwner2= new LocalOwner("bar","foo","go","http://ug.com");
        localOwnerDao.insert(localOwner1);
        localOwnerDao.insert(localOwner2);
        localOwnerDao.deleteAll();
        localOwnerDao.getAllUsers().observeForever(new Observer<List<LocalOwner>>() {
            @Override
            public void onChanged(List<LocalOwner> byName) {
                assertThat(byName.size(),equalTo(0));
                localOwnerDao.getAllUsers().removeObserver(this);
            }
        });

    }
}