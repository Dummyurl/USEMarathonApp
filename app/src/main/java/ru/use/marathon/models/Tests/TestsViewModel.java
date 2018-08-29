package ru.use.marathon.models.Tests;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ilyas on 24-Aug-18.
 */

public class TestsViewModel extends AndroidViewModel {

    private TestsRepository mRepository;

    private LiveData<List<TestCollection>> mAllTests;

    public TestsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TestsRepository(application);
        mAllTests = mRepository.getAllTasksByNumber();

    }

    public LiveData<List<TestCollection>> getAllTests(){ return mAllTests;}

    public void deleteAll(){
        mRepository.deleteAll();
    }

    public void insert(TestCollection collection) { mRepository.insert(collection);}

}
