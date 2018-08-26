package ru.use.marathon.models.Tests;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by ilyas on 24-Aug-18.
 */

@Dao
public interface TestsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TestCollection collection);

    @Query("DELETE FROM tests")
    void deleteAll();

    @Query("SELECT * from tests ORDER BY task_number ASC")
    LiveData<List<TestCollection>> getAllTasksByNumber();


}
