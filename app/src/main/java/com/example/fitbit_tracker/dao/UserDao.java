package com.example.fitbit_tracker.dao;

import androidx.room.Query;
import androidx.room.Transaction;

import com.example.fitbit_tracker.model.relation.UserWithSessions;

import java.util.List;

public interface UserDao {

    @Transaction
    @Query("SELECT * FROM User")
    List<UserWithSessions> getUsersWithSessions();

}
