package com.restapi.restapi.model;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyNews {
    private int id; // id
    private String related;   // Related stocks and companies mentioned in the article. Symbol
    private String category;   // news category
    private Date datetime;   // Published time in UNIX timestamp
    private String headline;  // News headline
    private String image;   // Thumbnail image URL.
    private String source;  // News source.
    private String summary;  // News summary.
    private String url;  // URL of the original article.
}
