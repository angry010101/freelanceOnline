package com.example.angry.project2345;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by S1 on 2/9/2017.
 */
@IgnoreExtraProperties
public class UserInfoClass{
    public static class UserInfo extends Object implements Serializable {
        private String number="";
        private String name="";
        private Date date;
        private int positivemarks,negativemarks;
        private ArrayList<TaskInfo> tasksinfo = new ArrayList<TaskInfo>();

        public UserInfo() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public int getNegativemarks() {
            return negativemarks;
        }

        public void setNegativemarks(int negativemarks) {
            this.negativemarks = negativemarks;
        }

        public int getPositivemarks() {
            return positivemarks;
        }

        public void setPositivemarks(int positivemarks) {
            this.positivemarks = positivemarks;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public ArrayList<TaskInfo> getTasksInfo() {
            return tasksinfo;
        }

        public void setTasksInfo(ArrayList<TaskInfo> tasksInfo) {
            this.tasksinfo = tasksInfo;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }

    public static class TaskInfo{
        private String name="";
        private Double lat,lon;
        private Date date;
        private String description="";
        private String reward="";
        private String uId="";

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getuId() {
            return uId;
        }

        public void setuId(String uId) {
            this.uId = uId;
        }

        public Date getDate() {
            return date;
        }

        public Double getLat() {
            return lat;
        }

        public Double getLon() {
            return lon;
        }

        public String getReward() {
            return reward;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public String getDescription() {
            return description;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public String getName() {
            return name;
        }
        public void setDate(Date date) {
            this.date = date;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
