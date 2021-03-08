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
    fun getChildListByParentId(parentId: Int): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE parentId != -1")
    fun getChildList(): Single<List<Category>>;


    @Query("SELECT * FROM categories WHERE commonly = 1")
    fun getCommonlyList(): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: Int): Single<Category>;

    @Query("SELECT * FROM categories WHERE name = :name")
    fun getCategoryByName(name: String): Single<Category>;

    @Query("SELECT * FROM categories WHERE :whereCase")
    fun query(whereCase: String): Single<List<Category>>

    @Query("DELETE FROM categories WHERE id = :id")
    fun deleteById(id: Int): Completable

    @Query("DELETE FROM categories WHERE parentId = :parentId")
    fun deleteChildByParentId(parentId: Int): Completable





    @Update
    fun update(category: Category): Completable
}