/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.isdemu.spring;

import java.util.Properties;
import javax.activation.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 *
 * @author Jose Eduardo
 */
@Configuration
@ComponentScan("com.isdemu")
@EnableWebMvc
@EnableTransactionManagement


public class WebAppConfig extends WebMvcConfigurerAdapter{
    
    public javax.sql.DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
/*
		dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		dataSource.setUrl("jdbc:oracle:thin:@172.16.1.46:1521:SIIT");
		dataSource.setUsername("siit");
		dataSource.setPassword("new2010siit33");*/

		return dataSource;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan("sv.gob.isdemu.model");
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		return sessionFactoryBean;
	}

	final Properties hibernateProperties() {
        return new Properties() {
            private static final long serialVersionUID = 1L;
            {
                setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
                setProperty("hibernate.cache.use_second_level_cache", "false");
                setProperty("hibernate.cache.use_query_cache", "false");
                setProperty("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
                setProperty("hibernate.cglib.use_reflection_optimizer", "false");
                setProperty("hibernate.connection.release_mode", "after_transaction"); //IMPORTANTE PARA QUE LOS TEST NO FALLEN
                setProperty("hibernate.show_sql", "false");
                setProperty("hibernate.connection.autocommit", "false");                                
            }
        };
    }

	@Bean
	public HibernateTransactionManager transactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory().getObject());
		return transactionManager;
	}

	@Bean
	public UrlBasedViewResolver setupViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix("/WEB-INF/pages/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		return resolver;
	}
	
	@Override  
    public void addResourceHandlers(ResourceHandlerRegistry registry) {  
           
            registry.addResourceHandler("/assets/**").addResourceLocations("/assets/"); 
    }  
}
