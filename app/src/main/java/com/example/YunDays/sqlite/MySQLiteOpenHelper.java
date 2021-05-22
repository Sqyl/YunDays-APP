package com.example.YunDays.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 *  MySQLiteOpenHelper 单例模式 BY SQYL 物联NZ171王畅
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static SQLiteOpenHelper mInstance;
    public static synchronized SQLiteOpenHelper getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new MySQLiteOpenHelper(context, "YunDays", null, 1);
        }
        return mInstance;
    }

    private MySQLiteOpenHelper(@Nullable Context context, @Nullable String name,
                              @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * UserClass表
         * _id           integer  primary key    not null
         * userAccount   text       not null
         * userPassword  text       not null
         * userName      text       not null
         *
         */
        String userclass =
                "CREATE TABLE IF NOT EXISTS userclass (" +
                    "_id           INTEGER  PRIMARY KEY  NOT NULL," +
                    "useraccount   TEXT NOT NULL," +
                    "userpassword  TEXT NOT NULL," +
                    "username      TEXT NOT NULL);";
        /**
         * dayEvent表
         * _id           integer    primary key  not null autoincrement
         * name          text       not null
         * belong_UserID integer    not null
         * date          text       not null
         * time          text       not null
         */
        String dayevent =
                "CREATE TABLE IF NOT EXISTS dayevent (" +
                    "_id           INTEGER  PRIMARY KEY  NOT NULL," +
                    "name          TEXT NOT NULL," +
                    "belong_userid INTEGER  NOT NULL," +
                    "date          TEXT NOT NULL," +
                    "time          TEXT NOT NULL);";
        /**
         * classEvent表
         * _id           integer    primary key  not null autoincrement
         * name          text       not null
         * belong_UserID integer    not null
         * class_date    integer    not null
         * teacher       text
         * classroom     text
         * begin_class   integer    not null
         * end_class     integer
         */
        String classevent =
                "CREATE TABLE IF NOT EXISTS classevent (" +
                    "_id           INTEGER  PRIMARY KEY  NOT NULL," +
                    "name          TEXT NOT NULL," +
                    "belong_userid INTEGER  NOT NULL," +
                    "class_date    INEGER  NOT NULL," +
                    "teacher       TEXT NOT NULL," +
                    "classroom     TEXT NOT NULL," +
                    "begin_class   INTEGER  NOT NULL," +
                    "end_class     INTEGER  NOT NULL);";
        /**
         * dakaEvent表
         * _id           integer    primary key  not null autoincrement
         * name          text       not null
         * belong_UserID integer    not null
         * 由于sqlite没有boolean，所以这里用integer存储boolean变量，0位false，1位true
         * daka_today    integer    not null
         * last_days     integer    not null
         * type          text       not null
         * 存到sqlite数据库中时，将数组转换为text，以英文半角逗号为分隔符
         * daka_days     text
         */
        String dakaevent =
                "CREATE TABLE IF NOT EXISTS dakaevent (" +
                    "_id           INTEGER  PRIMARY KEY  NOT NULL," +
                    "name          TEXT     NOT NULL," +
                    "belong_userid INTEGER  NOT NULL," +
                    "daka_today    INTEGER  NOT NULL," +
                    "last_days     INTEGER  NOT NULL," +
                    "type          TEXT NOT NULL," +
                    "daka_days     TEXT );";
        db.execSQL(userclass);
        db.execSQL(dayevent);
        db.execSQL(classevent);
        db.execSQL(dakaevent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
