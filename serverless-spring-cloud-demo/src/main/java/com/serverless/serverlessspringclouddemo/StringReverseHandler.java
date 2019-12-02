package com.serverless.serverlessspringclouddemo;

import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

public class StringReverseHandler extends SpringBootRequestHandler<String, String> {
}
