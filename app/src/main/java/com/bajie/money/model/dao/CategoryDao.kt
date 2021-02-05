package com.bajie.money.model.dao

import androidx.room.Dao
import androidx.room.Query
import com.bajie.money.model.data.Category
import io.reactivex.Single

/**

 * bajie on 2021/1/14 17:04

 */
@Dao
interface CategoryDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertMovie(movie: MovieSubject)
//
//    @Query("SELECT count(*) FROM movies")
//    fun getCount(): Single<Int>
//
    @Query("SELECT * FROM categories")
    fun getList(): Single<List<Category>>;
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertAll(movies: List<MovieSubject>);
}