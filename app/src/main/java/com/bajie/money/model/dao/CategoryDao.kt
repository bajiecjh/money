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

    @Query("SELECT * FROM categories WHERE parentId = -1")
    fun getParentList(): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE parentId = -2")
    fun getInTypeList(): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE parentId = :parentId")
    fun getChildListByParentId(parentId: Int): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE parentId != -1 AND parentId != -2 LIMIT 1")
    fun getFirstOutChild(): Single<Category>;

    @Query("SELECT * FROM categories WHERE parentId == -2 LIMIT 1")
    fun getFirstInChild(): Single<Category>;

    @Query("SELECT * FROM categories WHERE commonly = 1 AND parentId != -2")
    fun getOutCommonlyList(): Single<List<Category>>;

    @Query("SELECT * FROM categories WHERE commonly = 1 AND parentId == -2")
    fun getInCommonlyList(): Single<List<Category>>;

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