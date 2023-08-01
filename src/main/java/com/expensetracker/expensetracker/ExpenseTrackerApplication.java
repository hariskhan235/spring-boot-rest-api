package com.expensetracker.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.expensetracker.expensetracker.filters.AuthFilters;

@SpringBootApplication
@ComponentScan(basePackages = "com.expensetracker")
public class ExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

	@Bean
	public FilterRegistrationBean<AuthFilters> filterRegistrationBean() {
		FilterRegistrationBean<AuthFilters> registrationBean = new FilterRegistrationBean<AuthFilters>();
		AuthFilters authFilters =  new AuthFilters();
		registrationBean.setFilter(authFilters);
		registrationBean.addUrlPatterns("/api/categories/*");
		return registrationBean;
	}

}
