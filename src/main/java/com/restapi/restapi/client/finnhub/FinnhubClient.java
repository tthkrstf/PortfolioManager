package com.restapi.restapi.client.finnhub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.restapi.dto.external.finnhub.FinnhubCompanyNewsRaw;
import com.restapi.restapi.dto.external.finnhub.FinnhubQuoteRaw;
import com.restapi.restapi.client.FinanceDataProvider;
import com.restapi.restapi.common.ApiConstants;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class FinnhubClient implements FinanceDataProvider {

    private final String apiKey;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public FinnhubClient(@Value("${finnhub.api.key}") String apiKey,
                         ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.objectMapper = objectMapper;
    }

    @Override
    public FinnhubQuoteRaw getQuoteRaw(String symbol) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(ApiConstants.FINNHUB_HOST)
                .addPathSegments(ApiConstants.QUOTE_PATH)
                .addQueryParameter("symbol", symbol)
                .addQueryParameter("token", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "PortfolioManager/1.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            if (response.body() == null) {
                throw new IOException("Empty response body from Finnhub");
            }

            String jsonResponse = response.body().string();
            return objectMapper.readValue(jsonResponse, FinnhubQuoteRaw.class);

        } catch (IOException e) {
            throw new RuntimeException("Error during API call to Finnhub", e);
        }
    }

    @Override
    public List<FinnhubCompanyNewsRaw> getCompanyNewsRaw(String symbol){
        LocalDate today = LocalDate.now();
        LocalDate daysAgo = today.minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String fromDate = today.format(formatter);
        String toDate = today.format(formatter);


        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host(ApiConstants.FINNHUB_HOST)
                .addPathSegments(ApiConstants.COMPANY_NEWS_PATH)
                .addQueryParameter("symbol", symbol)
                .addQueryParameter("from", fromDate)
                .addQueryParameter("to", toDate)
                .addQueryParameter("token", apiKey)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "PortfolioManager/1.0")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            if (response.body() == null) {
                throw new IOException("Empty response body from Finnhub");
            }

            String jsonResponse = response.body().string();

            return objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<List<FinnhubCompanyNewsRaw>>() {}
            );

        } catch (IOException e) {
            throw new RuntimeException("Error during API call to Finnhub", e);
        }
    }
}
