package com.example.YunDays.event;

/**
 * Designed by Sqyl NZ171王畅
 * extends:
 *    protected String name;
 *    protected int belong_UserID;
 *     and their get/set.
 */

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

public class classEvent extends event implements Comparable<classEvent>{

    public static final int Sunday = 1;
    public static final int Monday = 2;
    public static final int Tuesday = 3;
    public static final int Wednesday = 4;
    public static final int Thursday = 5;
    public static final int Friday = 6;
    public static final int Saturday = 7;

    //课程在每周的周几
    private int class_date;
    //授课老师
    private String class_teacher;
    //教室
    private String classRoom;
    //从第几节课开始
    private int begin_class;
    //到第几节课结束
    private int end_class;

    public classEvent(int _id, String name, int belong_UserID,
                           int class_date, String class_teacher, String classRoom,
                           int begin_class, int end_class) {
        this.set_id(_id);
        this.setName(name);
        this.setBelong_userID(belong_UserID);
        this.setClass_date(class_date);
        this.setClass_teacher(class_teacher);
        this.setClassRoom(classRoom);
        this.setBegin_class(begin_class);
        setEnd_class(end_class);
    }

    public classEvent(int _id, String name, int belong_UserID,
                      int class_date, String class_teacher, String classRoom,
                      int begin_class) {
        this.set_id(_id);
        this.setName(name);
        this.setBelong_userID(belong_UserID);
        this.setClass_date(class_date);
        this.setClass_teacher(class_teacher);
        this.setClassRoom(classRoom);
        this.setBegin_class(begin_class);
        this.setEnd_class(-1);
    }

    public void setBegin_class(int begin_class) { this.begin_class = begin_class; }
    public void setClass_date(int class_date) { this.class_date = class_date; }
    public void setClass_teacher(String class_teacher) { this.class_teacher = class_teacher; }
    public void setClassRoom(String classRoom) { this.classRoom = classRoom; }
    public void setEnd_class(int end_class) { this.end_class = end_class; }

    public int getClass_date() { return this.class_date; }
    public String getClass_teacher() { return this.class_teacher; }
    public String getClassRoom() { return this.classRoom; }
    public int getBegin_class() { return this.begin_class; }
    public int getEnd_class() { return this.end_class; }


    @Override
    public int compareTo(classEvent o) {
        if (this.begin_class > o.begin_class)
            return 1;
        else if (this.begin_class == o.begin_class)
            return 0;
        else return -1;
    }

    @Override
    public String toString() {
        return "classEvent{" +
                "class_date=" + class_date +
                ", class_teacher='" + class_teacher + '\'' +
                ", classRoom='" + classRoom + '\'' +
                ", begin_class=" + begin_class +
                ", end_class=" + end_class +
                ", name='" + name + '\'' +
                ", belong_userID=" + belong_userID +
                ", _id=" + _id +
                '}';
    }
}
