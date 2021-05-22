package com.example.YunDays.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.YunDays.event.classEvent;
import com.example.YunDays.event.dakaEvent;
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.userclass.UserClass;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EventSQLiteOperation {
    private static final String TAG = "Sqyl";

    /**
     * UserClass表
     * _id           integer  primary key    not null
     * userAccount   text       not null
     * userPassword  text       not null
     * userName      text       not null
     *
     */
    public EventSQLiteOperation() {}

    public UserClass searchUserClass(Context context) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM userclass", null);
            if(cursor.moveToFirst()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String userAccount = cursor.getString(cursor.getColumnIndex("useraccount"));
                String userPassword = cursor.getString(cursor.getColumnIndex("userpassword"));
                String userName = cursor.getString(cursor.getColumnIndex("username"));
                cursor.close();
                db.close();
                return new UserClass(_id, userAccount, userPassword, userName);
            }
        }
        return null;
    }

    public void insertUserClass(Context context, UserClass userClass) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "INSERT INTO userclass(_id, useraccount, userpassword, username)" + "VALUES('"
                    + userClass.getUserID() + "','"
                    + userClass.getUserAccount() + "','"
                    + userClass.getUserPassword() + "','"
                    + userClass.getUserName() + "');";
            db.execSQL(sql);
        }
        db.close();
    }

    public boolean deleteUserClass(Context context) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "DELETE FROM userclass";
            db.execSQL(sql);
            return true;
        }
        db.close();
        return false;
    }

    /**
     * dayEvent表
     * _id           integer    primary key  not null autoincrement
     * name          text       not null
     * belong_UserID integer    not null
     * date          text       not null
     * time          text       not null
     */

    public List<dayEvent> searchDayEvent(Context context, String date) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<dayEvent> res = new ArrayList<>();
        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM dayevent WHERE date='" + date + "'",
                    null);
            while(cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int belong_UserID = cursor.getInt(cursor.getColumnIndex("belong_userid"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                res.add(new dayEvent(_id, name, belong_UserID, date, time));
            }
            cursor.close();
            db.close();
            return res;
        }
        return null;
    }

    public void insertDayEvent(Context context, dayEvent dayEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "INSERT INTO dayevent(_id, name, belong_userid, date, time)" + "VALUES('"
                    + dayEvent.get_id() + "','"
                    + dayEvent.getName() + "','"
                    + dayEvent.getBelong_userID() + "','"
                    + dayEvent.getDate() + "','"
                    + dayEvent.getTime() + "');";
            db.execSQL(sql);
        }
        db.close();
    }

    public void updateDayEvent(Context context, dayEvent dayEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "UPDATE dayevent " +
                    "SET name='" + dayEvent.getName() + "'," +
                    "date='" + dayEvent.getDate() + "'," +
                    "time='" + dayEvent.getTime() + "' " +
                    "WHERE _id=" + dayEvent.get_id();
            db.execSQL(sql);
        }
        db.close();
    }

    public boolean deleteDayEvent(Context context, dayEvent dayEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "DELETE FROM dayevent WHERE"
                        + " _id = '" + dayEvent.get_id() + "';";
            db.execSQL(sql);
            db.close();
            return true;
        }
        return false;
    }

    public boolean deleteAllDayEvent(Context context) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "DELETE FROM dayevent" + ";";
            db.execSQL(sql);
            db.close();
            return true;
        }
        return false;
    }

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
    public List<classEvent> searchClassEvent(Context context,int class_date) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<classEvent> res = new ArrayList<>();
        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM classevent WHERE class_date='"
                            + class_date + "'",null);
            while(cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int belong_UserID = cursor.getInt(cursor.getColumnIndex("belong_userid"));
                String class_teacher = cursor.getString(cursor.getColumnIndex("teacher"));
                String classroom = cursor.getString(cursor.getColumnIndex("classroom"));
                int begin_class = cursor.getInt(cursor.getColumnIndex("begin_class"));
                int end_class = cursor.getInt(cursor.getColumnIndex("end_class"));
                res.add(new classEvent(_id, name, belong_UserID, class_date, class_teacher, classroom,
                        begin_class, end_class));
            }
            cursor.close();
            db.close();
            return res;
        }
        return null;
    }

    public void insertClassEvent(Context context, classEvent classEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "INSERT INTO classevent(_id, name, belong_userid, class_date, teacher," +
                    " classroom, begin_class, end_class)" +
                    "VALUES('"
                    + classEvent.get_id() + "','"
                    + classEvent.getName() + "','"
                    + classEvent.getBelong_userID() + "','"
                    + classEvent.getClass_date() + "','"
                    + classEvent.getClass_teacher() + "','"
                    + classEvent.getClassRoom() + "','"
                    + classEvent.getBegin_class() + "','"
                    + classEvent.getEnd_class() + "');";
            db.execSQL(sql);
        }
        db.close();
    }

    public void updateClassEvent(Context context, classEvent classEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "UPDATE classevent " +
                    "SET name='" + classEvent.getName() + "'," +
                    "class_date=" + classEvent.getClass_date() + "," +
                    "teacher='" + classEvent.getClass_teacher() + "'," +
                    "classroom='" + classEvent.getClassRoom() + "'," +
                    "begin_class=" + classEvent.getBegin_class() + "," +
                    "end_class=" + classEvent.getEnd_class() + " " +
                    "WHERE _id=" + classEvent.get_id();
            db.execSQL(sql);
        }
        db.close();
    }

    public boolean deleteClassEvent(Context context, classEvent classEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "DELETE FROM classevent WHERE "
                        + "_id='" + classEvent.get_id() + "';";
            db.execSQL(sql);
            db.close();
            return true;
        }
        return false;
    }

    public boolean deleteAllClassEvent(Context context) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "DELETE FROM classevent" + ";";
            db.execSQL(sql);
            db.close();
            return true;
        }
        return false;
    }

    /**
     * dakaEvent表
     * _id           integer    primary key  not null autoincrement
     * name          text       not null
     * belong_UserID integer    not null
     * 由于sqlite没有boolean，所以这里用integer存储boolean变量，0位false指今天未打卡，1位true指今天已打卡
     * daka_today    integer    not null
     * last_days     integer    not null
     * type          text       not null
     * 存到sqlite数据库中时，将数组转换为text，以英文半角逗号为分隔符
     * daka_days     text
     */
    public List<dakaEvent> searchDakaEvent(Context context) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        List<dakaEvent> res = new ArrayList<>();
        if(db.isOpen()) {
            Cursor cursor = db.rawQuery("SELECT * FROM dakaevent", null);
            while(cursor.moveToNext()) {
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                int belong_UserID = cursor.getInt(cursor.getColumnIndex("belong_userid"));
                boolean daka_today;
                if(cursor.getInt(cursor.getColumnIndex("daka_today"))
                        == dakaEvent.TODAY_HAVE_DAKAED)
                    daka_today = true;
                else daka_today = false;
                int last_days = cursor.getInt(cursor.getColumnIndex("last_days"));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String daka_days_text = cursor.getString(cursor.getColumnIndex("daka_days"));
                String[] daka_days_element = daka_days_text.split(",");
                List<String> daka_days = new ArrayList<>();
                for(String element: daka_days_element) {
                    daka_days.add(element);
                }
                res.add(new dakaEvent(_id, type, name, belong_UserID, last_days, daka_days));
            }
            cursor.close();
            db.close();
            return res;
        }
        return null;
    }

    public void insertDakaEvent(Context context, @NotNull dakaEvent dakaEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String daka_days = "";
        if(dakaEvent.getDaka_days() != null) {
            for(String days : dakaEvent.getDaka_days()) {
                daka_days += days + ",";
            }
        }
        if(db.isOpen()) {
            String sql = "INSERT INTO dakaevent(_id, name, belong_userid, daka_today, last_days," +
                    " type, daka_days)" + " VALUES('"
                    + dakaEvent.get_id() + "','"
                    + dakaEvent.getName() + "','"
                    + dakaEvent.getBelong_userID() + "','"
                    + dakaEvent.TODAY_HAVENOT_DAKA + "','"
                    + dakaEvent.getLast_days() + "','"
                    + dakaEvent.getType() + "','"
                    + daka_days +"');";
            db.execSQL(sql);
            Log.i(TAG, "insertDakaEvent: Success");
        }
        db.close();
    }

    public boolean deleteDakaEvent(Context context, dakaEvent dakaEvent) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "DELETE FROM dakaevent WHERE "
                    + "_id='" + dakaEvent.get_id() + "';";
            db.execSQL(sql);
            db.close();
            return true;
        }
        return false;
    }

    public boolean deleteAllDakaEvent(Context context) {
        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "DELETE FROM dakaevent" + ";";
            db.execSQL(sql);
            db.close();
            return true;
        }
        return false;
    }
}

