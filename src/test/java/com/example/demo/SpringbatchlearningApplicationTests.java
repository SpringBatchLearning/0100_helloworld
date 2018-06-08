package com.example.demo;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.config.BatchConfiguration;
import com.example.demo.listener.JobCompletionNotificationListener;

@RunWith(SpringRunner.class)
@SpringBootTest()
@Import({BatchConfiguration.class, JobCompletionNotificationListener.class})
@ActiveProfiles("test")
public class SpringbatchlearningApplicationTests {
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void testJob() throws Exception {
		//jdbcTemplate.update("delete from people");
		
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		
		Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
	
	}
	
	@Configuration
    static class BatchTestConfig {

        @Bean
        JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
        
        @Bean
    	public DataSource dataSource() {
        	SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
    		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    		dataSource.setUrl("jdbc:mysql://localhost:3306/springbatch");
    		dataSource.setUsername("root");
    		dataSource.setPassword("111111");
    		return dataSource;
    	}	
        
        @Bean
        public JdbcTemplate jdbcTemplate() {
        	JdbcTemplate jdbcTemplate = new JdbcTemplate();
        	jdbcTemplate.setDataSource(dataSource());
        	return jdbcTemplate;
        }
         
    }

}
