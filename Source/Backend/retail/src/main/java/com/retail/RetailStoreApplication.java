package com.retail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 *
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.retail.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.retail.repository.elasticsearch")
public class RetailStoreApplication 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        SpringApplication.run(RetailStoreApplication.class, args);
    }
}
