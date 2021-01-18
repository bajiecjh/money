package com.bajie.money.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bajie.money.model.data.MovieSubject
import io.reactivex.Single

/**

 * bajie on 2021/1/14 17:04

 */
@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieSubject)

    @Query("SELECT count(*) FROM movies")
    fun getCount(): Single<Int>

    @Query("SELECT * FROM movies")
    fun getList(): Single<List<MovieSubject>>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<MovieSubject>);
}