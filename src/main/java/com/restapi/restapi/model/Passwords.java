package com.restapi.restapi.model;

import lombok.Data;

import java.util.Date;

@Data
public class Passwords {
    public int id;
    public String name;
    public int loginId;
    public String password;
    public String url;
    public String notes;
    public Date deletedDate;
    public Date creationDate;
    public Date modificationDate;
}
