package com.restapi.restapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
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
}
