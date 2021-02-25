package com.bajie.money.model.dao

import androidx.room.*
import com.bajie.money.model.data.Category
import io.reactivex.Completable
import io.reactivex.Single

/**

 * bajie on 2021/1/14 17:04

 */
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(category: Category): Completable
//
//    @Query("SELECT count(*) FROM movies")
//    fun getCount(): Single<Int>
//
    @Query("SELECT * FROM categories WHERE parentId = -1")
    fun getList(): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE parentId = :parentId")
    fun getChildList(parentId: Int): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE commonly = 1")
    fun getCommonlyList(): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE :whereCase")
    fun query(whereCase: String): Single<List<Category>>

    @Update
    fun update(category: Category): Completable
}