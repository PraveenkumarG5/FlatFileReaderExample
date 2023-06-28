package com.tcs.poc.batch.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "appEntityManagerFactory",
        basePackages = "com.tcs.poc.batch.repository"
)
@EnableTransactionManagement
public class AppDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource appDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "appEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean appEntityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                          @Qualifier("appDataSource") DataSource appDataSource){

        return builder
                .dataSource(appDataSource)
                .packages("com.tcs.poc.batch.domain")
                .persistenceUnit("app")
                .build();
    }
}
