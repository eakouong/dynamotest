package com.yan.dynamotest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
public class DynamoDbConfiguration {

	@Bean
	DynamoDBMapper dynamoDBMapper() {
		return new DynamoDBMapper(buildAmazonDynamoDB());

	}

	private AmazonDynamoDB buildAmazonDynamoDB() {
	    return AmazonDynamoDBAsyncClientBuilder.standard()
	            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
	                    "dynamodb.ap-southeast-1.amazonaws.com", "ap-southeast-1"))
	            .withCredentials(new AWSStaticCredentialsProvider(
	                    new BasicAWSCredentials("AKIAQFI4U3I63F3I47VE", "TtP3D6rhlmRehd+3IvuXwijZNpOX0naDjhTDDTR0")))
	            .build();
	}
}
