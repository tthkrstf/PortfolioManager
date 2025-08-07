package com.restapi.restapi.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class CompanyNewsDTO {
    private int id;
    private String category;   // news category
    private Date datetime;   // Published time in UNIX timestamp
    private String headline;  // News headline
    private String image;   // Thumbnail image URL.
    private String related;   // Related stocks and companies mentioned in the article.
    private String source;  // News source.
    private String summary;  // News summary.
    private String url;  // URL of the original article.

    public CompanyNewsDTO(int id, String category, Date datetime, String headline,
                          String image, String related, String source,
                          String summary, String url) {
        this.id = id;
        this.category = category;
        this.datetime = datetime;
        this.headline = headline;
        this.image = image;
        this.related = related;
        this.source = source;
        this.summary = summary;
        this.url = url;
    }
}
