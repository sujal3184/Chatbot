package com.example.simplechatbot.Data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SessionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session)

    @Delete
    suspend fun deleteSession(session: Session)

    @Query("SELECT * FROM chatSessions WHERE userID = :userID")
    suspend fun getSessionsByUserID(userID: String): List<Session>

    // Keep this for admin purposes if needed
    @Query("SELECT * FROM chatSessions")
    suspend fun getAllSessions(): List<Session>
}