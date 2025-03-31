package com.example.simplechatbot.Data

class SessionRepository(private val sessionDao: SessionDAO) {
    suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    suspend fun getSessionsByUserID(userID: String): List<Session> {
        return sessionDao.getSessionsByUserID(userID)
    }

    suspend fun getAllSessions(): List<Session> {
        return sessionDao.getAllSessions()
    }
}