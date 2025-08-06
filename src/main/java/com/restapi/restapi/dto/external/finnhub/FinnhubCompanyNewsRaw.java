package com.restapi.restapi.dto.external.finnhub;

import lombok.Data;

@Data
public class FinnhubCompanyNewsRaw extends CompanyNewsRaw {
    private String category;   // news category
    private long datetime;   // Published time in UNIX timestamp
    private String headline;  // News headline
    private int id;   // News ID. This value can be used for minId params to get the latest news only
    private String image;   // Thumbnail image URL.
    private String related;   // Related stocks and companies mentioned in the article.
    private String source;  // News source.
    private String summary;  // News summary.
    private String url;  // URL of the original article.
}