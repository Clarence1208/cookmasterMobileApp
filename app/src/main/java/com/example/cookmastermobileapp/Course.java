package com.example.cookmastermobileapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class Course implements Parcelable {
    private String id;
    private String title;
    private String description;
    private Boolean validated;
    private Date startDate;
    private Date endDate;
    private String place;


    public Course(String id, String title, String description, boolean validated, String startDate, String endDate, String place) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.validated = validated;
        this.startDate = parseDate(startDate);
        this.endDate = parseDate(endDate);
        this.place = place;
    }

    protected Course(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        byte tmpValidated = in.readByte();
        validated = tmpValidated == 0 ? null : tmpValidated == 1;
        place = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    private Date parseDate(String dateString) {
        if (dateString.endsWith("Z")) {
            dateString = dateString.substring(0, dateString.length() - 1);
        }else{
            dateString = dateString.substring(0, dateString.length() - 6);
        }
        // remove the T between date and time
        dateString = dateString.replace("T", " ");
        // parse the date string of this format 2023-05-16 06:31:04
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isCompleted() {
        return validated;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Date getStartDate() {
        return startDate;
    }
    public String getStartDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(startDate);
    }

    public String getEndDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(endDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeByte((byte) (validated == null ? 0 : validated ? 1 : 2));
        parcel.writeString(place);
    }
}

