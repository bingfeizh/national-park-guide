package com.hfad.nationalparksguide.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ParkInfoDao {
    @Query("SELECT * FROM parkinfo")
    List<ParkInfo> findAll();

    @Query("SELECT * FROM parkinfo WHERE park_name LIKE :parkName LIMIT 1")
    ParkInfo findByName(String parkName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ParkInfo... parkInfo);

    @Delete
    void delete(ParkInfo parkInfo);
}

