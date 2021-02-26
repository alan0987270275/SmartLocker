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

@DynamoDBTable(tableName = "myfristaws-mobilehub-1927978180-UserProfile")

public class UserProfileDO {
    private String _userid;
    private String _photo;

    @DynamoDBHashKey(attributeName = "Userid")
    @DynamoDBIndexHashKey(attributeName = "Userid", globalSecondaryIndexName = "Userid-Photo")
    public String getUserid() {
        return _userid;
    }

    public void setUserid(final String _userid) {
        this._userid = _userid;
    }
    @DynamoDBRangeKey(attributeName = "Photo")
    @DynamoDBIndexRangeKey(attributeName = "Photo", globalSecondaryIndexName = "Userid-Photo")
    public String getPhoto() {
        return _photo;
    }

    public void setPhoto(final String _photo) {
        this._photo = _photo;
    }

}
