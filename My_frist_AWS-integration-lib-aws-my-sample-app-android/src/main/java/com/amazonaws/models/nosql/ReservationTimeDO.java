package com.amazonaws.models.nosql;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "myfristaws-mobilehub-1927978180-ReservationTime")

public class ReservationTimeDO {
    private String _user;
    private String _time;
    private Double _locker;

    @DynamoDBHashKey(attributeName = "user")
    @DynamoDBAttribute(attributeName = "user")
    public String getUser() {
        return _user;
    }

    public void setUser(final String _user) {
        this._user = _user;
    }
    @DynamoDBRangeKey(attributeName = "Time")
    @DynamoDBAttribute(attributeName = "Time")
    public String getTime() {
        return _time;
    }

    public void setTime(final String _time) {
        this._time = _time;
    }
    @DynamoDBAttribute(attributeName = "Locker")
    public Double getLocker() {
        return _locker;
    }

    public void setLocker(final Double _locker) {
        this._locker = _locker;
    }

}
