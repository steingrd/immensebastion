package com.github.steingrd.bloominghollows.config;

import java.net.URI;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DbConfiguration {

	@Bean
	public URI dbUrl() {
		String fromEnvironment = System.getenv("DATABASE_URL");
		
		// no env variable? try system property
		if (fromEnvironment == null) {
			fromEnvironment = System.getProperty("DATABASE_URL");
		}
		
		// still nothing? let's just fail
		if (fromEnvironment == null) {
			throw new RuntimeException("Could not find environment variable or system property named 'DATABASE_URL'.");
		}
		return URI.create(fromEnvironment);
	}
	
	@Bean
	public DataSource dataSource(URI dbUrl) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://" + dbUrl.getHost() + ":" + dbUrl.getPort() + dbUrl.getPath());
		dataSource.setUsername(dbUrl.getUserInfo().split(":")[0]);
		dataSource.setPassword(dbUrl.getUserInfo().split(":")[1]);
		dataSource.setDriverClassName("org.postgresql.Driver");
		return dataSource;
	}
	
	@Bean
	public SessionFactory sessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionFactoryBuilder = new LocalSessionFactoryBuilder(dataSource);
		sessionFactoryBuilder.scanPackages("com.github.steingrd.bloominghollows");
		sessionFactoryBuilder.addProperties(hibernateProperties());
		return sessionFactoryBuilder.buildSessionFactory();
	}

	@Bean
	public Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect",  "org.hibernate.dialect.PostgreSQLDialect");
        //hibernateProperties.setProperty("hibernate.show_sql", "true");
        //hibernateProperties.setProperty("hibernate.format_sql", "true");
        return hibernateProperties;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		SessionFactory sessionFactory = sessionFactory(dataSource(dbUrl()));
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}	

	
}
