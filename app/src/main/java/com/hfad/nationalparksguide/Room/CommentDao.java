package com.hfad.nationalparksguide.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface CommentDao {
    @Query("SELECT * FROM comment")
    List<Comment> findAllComments();

    @Query("SELECT uid, user_name, comment, longitude, latitude" +
            " FROM ( " +
            "    SELECT *, " +
            "        1609.34 * 3956 * 2 * " +
            "            ASIN( SQRT( POWER( SIN( (:userLatitude - abs(latitude)) " +
            "               * pi() / 180 / 2), 2) + " +
            "               COS(55.170000 * pi() / 180 ) *" +
            "               COS(abs(latitude) * pi() / 180) " +
            "               * POWER(SIN((:userLongitude - (longitude)) *  pi() / 180 / 2), 2)" +
            "                )) as result" +
            "    FROM comment order by result asc limit 20" +
            ") AS L")
    List<Comment> commentsFindByLocation(double userLongitude, double userLatitude);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment... comment);

    @Delete
    void delete(Comment comment);

//   TODO: more query needed for FireBase? please specify:
}

