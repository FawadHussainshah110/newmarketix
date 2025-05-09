package com.example.marketix.domain.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by zeeshan on 04/09/2020.
 */
@Entity(tableName = "dummy_user")
public class DummyUser {

    @ColumnInfo(name = "created_at")
    public String createdAt;

    @PrimaryKey
    public Long id;

    public String name;

    @ColumnInfo(name = "updated_at")
    public String updatedAt;


    @Override
    public String toString() {
        return "DummyUser{" +
                "createdAt='" + createdAt + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }


}
