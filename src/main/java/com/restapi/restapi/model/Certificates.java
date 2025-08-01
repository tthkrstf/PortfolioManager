package com.restapi.restapi.model;

import lombok.Data;

import java.util.Date;

@Data
public class Certificates {
    public int id;
    public String name;
    public String certificates;
    public String servername;
    public String notes;
    public Date deletedDate;
    public Date creationDate;
    public Date modificationDate;
}
