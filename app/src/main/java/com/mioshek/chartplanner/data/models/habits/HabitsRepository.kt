package com.mioshek.chartplanner.data.models.habits

import kotlinx.coroutines.flow.Flow


interface HabitsRepository {
    fun getAllHabitsStream(): Flow<List<Habit>>

    fun getHabitStream(id: Int): Flow<Habit?>

    fun getAllHabitsByDateStream(date: Int): Flow<List<Habit>>

    suspend fun insert(habit: Habit)

    suspend fun delete(h_id: Int)

    suspend fun update(habit: Habit)
}

class OfflineHabitsRepository(private val habitDao: HabitDao) : HabitsRepository {
    override fun getAllHabitsStream(): Flow<List<Habit>> = habitDao.getAll()

    override fun getHabitStream(id: Int): Flow<Habit?> = habitDao.get(id)
    override fun getAllHabitsByDateStream(date: Int): Flow<List<Habit>> = habitDao.getAllByDate(date)

    override suspend fun insert(habit: Habit) = habitDao.insert(habit)

    override suspend fun delete(h_id: Int) = habitDao.delete(h_id)

    override suspend fun update(habit: Habit) = habitDao.update(
        habit.hId,
        habit.name,
        habit.description,
        habit.startEpochDate,
        habit.startEpochTime,
        habit.endEpochDate,
        habit.endEpochTime,
        habit.intervalDays,
    )
}