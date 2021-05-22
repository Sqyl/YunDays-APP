package com.example.YunDays.event;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.YunDays.request.DakaEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.ui.daka.SetDakaMsgActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Designed by Sqyl NZ171王畅
 * extends:
 *    protected String name;
 *    protected int belong_UserID;
 *     and their get/set.
 */

/**
 * dakaEvent表
 * _id           integer    primary key  not null autoincrement
 * name          text       not null
 * belong_UserID integer    not null
 * 由于sqlite没有boolean，所以这里用integer存储boolean变量，0为false，即未打卡，1为true，即已打卡
 * daka_today    integer    not null
 * last_days     integer    not null
 * type          text       not null
 * 存到sqlite数据库中时，将数组转换为text，以英文半角逗号为分隔符
 * daka_days     text
 */

public class dakaEvent extends event implements daka {
    /*  extends:
     *    protected String name;
     *    protected int belong_UserID;
     *   and their get/set.
     * */
    //判断今天是否打卡
    private boolean daka_today = false;
    public static final int TODAY_HAVENOT_DAKA = 0;
    public static final int TODAY_HAVE_DAKAED = 1;

    public static final String DAKA_LEARN = "learn";
    public static final String DAKA_HOBBY = "hobby";
    public static final String DAKA_SPORT = "sport";
    public static final String DAKA_READ = "read";
    public static final String DAKA_SLEEPEARLY = "sleepearly";
    //持续时间
    private int last_days;
    private String type;
    private List<String> daka_days = new ArrayList<>();

    public dakaEvent(int _id, String type, String name, int belongUserID, int last_days,
                     List<String> daka_days) {
        this.set_id(_id);
        this.setDaka_days(daka_days);
        Calendar c = Calendar.getInstance();
        String daka_day = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
                + c.get(Calendar.DAY_OF_MONTH);
        if(daka_days.contains(daka_day))
            this.setDaka_today(true);
        else this.setDaka_today(false);
        this.setType(type);
        this.setName(name);
        this.setBelong_userID(belongUserID);
        this.setLast_days(last_days);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void daka(Context context) {
        if(this.isDaka_today()) {
            Toast.makeText(context, "今日已打卡", Toast.LENGTH_SHORT).show();
        } else if(last_days == 0) {
            Toast.makeText(context, "该打卡项已无剩余打卡机会！", Toast.LENGTH_SHORT).show();
        } else{
            Intent intent_dakamsg = new Intent(context, SetDakaMsgActivity.class);
            intent_dakamsg.putExtra("_id", this.get_id());
            context.startActivity(intent_dakamsg);
            EventSQLiteOperation operation = new EventSQLiteOperation();
            operation.deleteDakaEvent(context, this);
            this.setDaka_today(true);
            this.setLast_days(this.last_days - 1);
            Calendar c = Calendar.getInstance();
            String daka_day = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
                    + c.get(Calendar.DAY_OF_MONTH);
            this.daka_days.add(daka_day);
            operation.insertDakaEvent(context, this);
            DakaEventRequest.updateDakaEvent(context, this);
        }

    }

    public boolean isDaka_today() { return daka_today; }
    public void setDaka_today(boolean daka_today) {
        this.daka_today = daka_today;
    }
    public void setLast_days(int last_days) {
        this.last_days = last_days;
    }
    public void setDaka_days(List<String> daka_days) { this.daka_days = daka_days; }

    public int getLast_days() {
        return last_days;
    }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<String> getDaka_days() { return daka_days; }
}
