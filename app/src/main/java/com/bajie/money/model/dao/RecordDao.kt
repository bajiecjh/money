package com.bajie.money.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.bajie.money.model.data.Record
import io.reactivex.Completable

/**

 * bajie on 2021/3/24 16:58

 */
@Dao
interface RecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(record: Record): Completable
}