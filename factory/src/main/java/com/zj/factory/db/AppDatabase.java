package com.zj.factory.db;


import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by JuQiu
 * on 16/6/25.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "brithdays";

    public static final int VERSION = 1;
}
