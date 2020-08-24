package com.orange.githubmash;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.orange.githubmash.data.local.LocalDatabase;
import com.orange.githubmash.data.local.LocalGitRepoDao;
import com.orange.githubmash.data.local.LocalGitRepoModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class LocalGitRepoDaoTest {

    private LocalGitRepoDao localGitRepoDao;
    private LocalDatabase db;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();


    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, LocalDatabase.class).build();
        localGitRepoDao = db.repoDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertandreadrepos() throws Exception {
        final LocalGitRepoModel localGitRepoModel= new LocalGitRepoModel("foo","bar","cat","go","http://ug.com",234);
        localGitRepoDao.insert(localGitRepoModel);
        localGitRepoDao.getAllRepos().observeForever(new Observer<List<LocalGitRepoModel>>() {
            @Override
            public void onChanged(List<LocalGitRepoModel> byName) {
                assertThat(byName.get(0).getName(), equalTo(localGitRepoModel.getName()));
                assertThat(byName.get(0).getEntry_owner(), equalTo(localGitRepoModel.getEntry_owner()));
                assertThat(byName.get(0).getOwner(), equalTo(localGitRepoModel.getOwner()));
                localGitRepoDao.getAllRepos().removeObserver(this);
            }
        });
        }
    @Test
    public void insertandreadmyrepos() throws Exception {
        final LocalGitRepoModel localGitRepoModel1= new LocalGitRepoModel("foo","bar","cat","go","http://ug.com",234);
        final LocalGitRepoModel localGitRepoModel2= new LocalGitRepoModel("bar","foo","cat","go","http://ug.com",234);
        localGitRepoDao.insert(localGitRepoModel1);
        localGitRepoDao.insert(localGitRepoModel2);
        localGitRepoDao.getmyReps("foo").observeForever(new Observer<List<LocalGitRepoModel>>() {
            @Override
            public void onChanged(List<LocalGitRepoModel> byName) {
                assertThat(byName.get(0).getName(), equalTo(localGitRepoModel1.getName()));
                assertThat(byName.get(0).getEntry_owner(), equalTo(localGitRepoModel1.getEntry_owner()));
                assertThat(byName.get(0).getOwner(), equalTo(localGitRepoModel1.getOwner()));
                localGitRepoDao.getmyReps("foo").removeObserver(this);
            }
        });
    }
    @Test
    public void deleterepo() throws Exception {
        final LocalGitRepoModel localGitRepoModel1= new LocalGitRepoModel("foo","bar","cat","go","http://ug.com",234);
        final LocalGitRepoModel localGitRepoModel2= new LocalGitRepoModel("bar","foo","cat","go","http://ug.com",234);
        localGitRepoDao.insert(localGitRepoModel1);
        localGitRepoDao.insert(localGitRepoModel2);
        localGitRepoDao.deleterep(localGitRepoModel1);
        localGitRepoDao.getAllRepos().observeForever(new Observer<List<LocalGitRepoModel>>() {
            @Override
            public void onChanged(List<LocalGitRepoModel> byName) {
                assertThat(byName.get(0).getName(), equalTo(localGitRepoModel2.getName()));
                assertThat(byName.get(0).getEntry_owner(), equalTo(localGitRepoModel2.getEntry_owner()));
                assertThat(byName.get(0).getOwner(), equalTo(localGitRepoModel2.getOwner()));
                assertThat(byName.size(),equalTo(1));
                localGitRepoDao.getAllRepos().removeObserver(this);
            }
        });
    }
    @Test
    public void deleteall() throws Exception {
        final LocalGitRepoModel localGitRepoModel1= new LocalGitRepoModel("foo","bar","cat","go","http://ug.com",234);
        final LocalGitRepoModel localGitRepoModel2= new LocalGitRepoModel("bar","foo","cat","go","http://ug.com",234);
        localGitRepoDao.insert(localGitRepoModel1);
        localGitRepoDao.insert(localGitRepoModel2);
        localGitRepoDao.deleteAll();
        localGitRepoDao.getAllRepos().observeForever(new Observer<List<LocalGitRepoModel>>() {
            @Override
            public void onChanged(List<LocalGitRepoModel> byName) {
                assertThat(byName.size(),equalTo(0));
                localGitRepoDao.getAllRepos().removeObserver(this);
            }
        });

    }
}



