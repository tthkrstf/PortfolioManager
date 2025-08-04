package com.restapi.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

@SpringBootApplication
public class RestapiApplication {

	public static void main(String[] args) {


		/*/
		String apiKey = "d26avohr01qh25lmnn2gd26avohr01qh25lmnn30";
		String symbol = "AAPL";
		//String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + apiKey;
		//String url = "https://finnhub.io/api/v1/stock/metric?symbol=" + symbol + "&metric=all&token=" + apiKey;
		String url = "https://finnhub.io/api/v1/company-news?symbol=AAPL&from=2025-01-15&to=2025-02-20&&token=" + apiKey;

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url(url)
				.build();

		try (Response response = client.newCall(request).execute()) {
			if (response.isSuccessful() && response.body() != null) {
				String jsonResponse = response.body().string();
				System.out.println("Finnhub API response:");
				System.out.println(jsonResponse);
			} else {
				System.err.println("Request failed with status code: " + response.code());
			}
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
		 */


        SpringApplication.run(RestapiApplication.class, args);
	}

}
