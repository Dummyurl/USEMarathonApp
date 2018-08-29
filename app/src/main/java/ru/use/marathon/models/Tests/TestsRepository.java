package ru.use.marathon.models.Tests;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ilyas on 24-Aug-18.
 */

public class TestsRepository {

    private TestsDAO mTestsDao;
    private LiveData<List<TestCollection>> mAllTests;

    TestsRepository(Application application){
        TestsRoomDatabase database = TestsRoomDatabase.getDatabase(application);
        mTestsDao = database.testsDAO();
        mAllTests = mTestsDao.getAllTasksByNumber();
    }

    LiveData<List<TestCollection>> getAllTasksByNumber(){
        return mAllTests;
    }

    public void insert (TestCollection collection){
        new insertAsyncTask(mTestsDao).execute(collection);
    }
    public void deleteAll(){
        new deleteAsyncTask(mTestsDao).execute();
    }


    private static class insertAsyncTask extends AsyncTask<TestCollection, Void, Void> {

        private TestsDAO mAsyncTaskDao;

        insertAsyncTask(TestsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TestCollection... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class deleteAsyncTask extends AsyncTask<TestCollection, Void, Void> {

        private TestsDAO mAsyncTaskDao;

        deleteAsyncTask(TestsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TestCollection... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}
