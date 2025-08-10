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
        SpringApplication.run(RestapiApplication.class, args);
	}

}
