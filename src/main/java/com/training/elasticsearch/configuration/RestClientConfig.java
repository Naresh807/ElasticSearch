package com.training.elasticsearch.configuration;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

	private String hostname = "my-elastic-training.es.us-west1.gcp.cloud.es.io";
	private Integer port = 9243;
	private String username = "elastic";
	private String password = "6z7w4tFOtPq9LSuUw8O3a3nl";

	@Override
	@Bean
	public RestHighLevelClient elasticsearchClient() {

		
		final ClientConfiguration clientConfiguration = 
				ClientConfiguration
				.builder()
				.connectedTo(hostname+":"+port)
				.usingSsl()
				.withBasicAuth(username, password)
				.build();

		return  RestClients
				.create(clientConfiguration)
				.rest();
	}
	
	//@Bean
	public RestHighLevelClient elasticClient() {
		
		
		//don't do if you run a local ES
		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, 
				               new UsernamePasswordCredentials(username,password));
		
		RestClientBuilder builder = RestClient.builder(
				              new HttpHost(hostname, port, "https"))
				              .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
								
								@Override
								public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
									return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)	;
								}
							});
		
		RestHighLevelClient client = new RestHighLevelClient(builder);
		return client;
	}	
   
}