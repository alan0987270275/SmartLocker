package com.example.alan_lin.smart_locker;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "myfristaws-mobilehub-1927978180-ReservationTime")

public class DBTime {

    private String _userId;
    private String _Time;
    private int _locker;
    private String _faceid;


    @DynamoDBHashKey(attributeName = "user")
    @DynamoDBAttribute(attributeName = "user")
    public String getUserId() {
        return _userId;
    }

    public void setUserId(final String _userId) {
        this._userId = _userId;
    }

    @DynamoDBRangeKey(attributeName = "Time")
    @DynamoDBAttribute(attributeName = "Time")

    public String getTime() {
        return _Time;
    }
    public void setTime(final String _articleId) {
        this._Time = _articleId;
    }

    @DynamoDBAttribute(attributeName = "Locker")
    public int getlocker() {
        return _locker;
    }
    public void setlocker(final int _locker) {
        this._locker = _locker;
    }

    @DynamoDBAttribute(attributeName = "FaceID")
    public String getfaceid() {
        return _faceid;
    }
    public void setfaceid(final String _faceid) {
        this._faceid = _faceid;
    }

    // setters and getters for other attribues ...

}
