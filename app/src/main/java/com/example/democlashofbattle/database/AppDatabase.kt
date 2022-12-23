package com.example.democlashofbattle.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.democlashofbattle.models.Player
import com.example.democlashofbattle.utils.CapabilityRoomConverter

@Database(entities = [Player::class], version = 1)
@TypeConverters(CapabilityRoomConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object {

        @Volatile
        var instance: AppDatabase? = null
            private set

        fun init(context: Context) {
            synchronized(this) {
                this.instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "playerDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}