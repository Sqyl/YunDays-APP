package com.example.YunDays.event;

/**
 * Designed by Sqyl NZ171王畅
 * extends:
 *    protected String name;
 *    protected int belong_UserID;
 *     and their get/set.
 */

/**
 * dayEvent表
 * _id           integer    primary key  not null autoincrement
 * name          text       not null
 * belong_UserID integer    not null
 * date          text       not null
 * time          text       not null
 */

public class dayEvent extends event{

    //日期
    private String date;
    //具体时间
    private String time;

    public dayEvent(int _id, String name, int belong_userID, String date, String time) {
        this.set_id(_id);
        this.setDate(date);
        this.setName(name);
        this.setBelong_userID(belong_userID);
        this.setTime(time);

    }

    public String getDate() { return date; }
    public String getTime() { return time; }

    public void setTime(String time) {
        this.time = time;
    }
    public void setDate(String date) { this.date = date; }

    @Override
    public String toString() {
        return "dayEvent{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", belong_userID=" + belong_userID +
                ", _id=" + _id +
                '}';
    }
}
