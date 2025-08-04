package com.restapi.restapi.client.finnhub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restapi.restapi.dto.external.finnhub.FinnhubQuoteRaw;
import com.restapi.restapi.mapper.FinnhubQuoteMapper;
import com.restapi.restapi.client.FinanceDataProvider;
import com.restapi.restapi.common.ApiConstants;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FinnhubClient implements FinanceDataProvider {

    private final String apiKey;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final FinnhubQuoteMapper quoteMapper;

    public FinnhubClient(@Value("${finnhub.api.key}") String apiKey,
                         ObjectMapper objectMapper,
                         FinnhubQuoteMapper quoteMapper) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient();
        this.objectMapper = objectMapper;
        this.quoteMapper = quoteMapper;
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
}
