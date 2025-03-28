package com.example.simplechatbot.Data

class SessionRepository(private val sessionDao : SessionDAO) {

    suspend fun insertSession(session: Session) {
        sessionDao.insertSession(session)
    }

    suspend fun deleteSession(session: Session) {
        sessionDao.deleteSession(session)
    }

    suspend fun getAllSessions(): List<Session> {
        return sessionDao.getAllSessions()
    }
}