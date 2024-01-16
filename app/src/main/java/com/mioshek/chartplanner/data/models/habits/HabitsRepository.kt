package com.mioshek.chartplanner.data.models.habits

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Habit] from a given data source.
 */
interface HabitsRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllHabitsStream(): Flow<List<Habit>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getHabitStream(id: Int): Flow<Habit?>

    /**
     * Insert item in the data source
     */
    suspend fun insertHabit(item: Habit)

    /**
     * Delete item from the data source
     */
    suspend fun deleteHabit(h_id: Int)

    /**
     * Update item in the data source
     */
    suspend fun updateHabit(item: Habit)
}

class OfflineHabitsRepository(private val itemDao: HabitDao) : HabitsRepository {
    override fun getAllHabitsStream(): Flow<List<Habit>> = itemDao.getAllHabits()

    override fun getHabitStream(id: Int): Flow<Habit?> = itemDao.getHabit(id)

    override suspend fun insertHabit(habit: Habit) = itemDao.insertHabit(habit)

    override suspend fun deleteHabit(h_id: Int) = itemDao.delete(h_id)

    override suspend fun updateHabit(habit: Habit) = itemDao.updateHabit(
        habit.hid,
        habit.name,
        habit.description,
        habit.completed,
        habit.date,
        habit.intervalDays
    )
}