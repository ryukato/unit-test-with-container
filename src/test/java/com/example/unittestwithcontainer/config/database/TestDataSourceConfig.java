package com.example.unittestwithcontainer.config.database;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.junit.ClassRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

// https://github.com/testcontainers/testcontainers-java/issues/417

@Configuration
@EnableJpaRepositories(
        basePackages = {"com.example.unittestwithcontainer.repo"},
        transactionManagerRef = "testDataSourceTransactionManager",
        entityManagerFactoryRef = "testDataSourceEntityManagerFactory"
)
public class TestDataSourceConfig {
    private final MySQLContainer mySQLContainer =  new MySQLContainer()
            .withDatabaseName("test_database")
            .withUsername("test_admin")
            .withPassword("password");

    @PostConstruct
    public void startContainer() {
        if (!mySQLContainer.isRunning()) {
            mySQLContainer.start();
        }
    }

    @PreDestroy
    public void stopContainer() {
        if (mySQLContainer.isRunning()) {
            mySQLContainer.stop();
        }
    }

    @Bean
    DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl( // mysql container
                "jdbc:mysql://localhost:"+mySQLContainer.getMappedPort(3306)+"/"+mySQLContainer.getDatabaseName()+"?&verifyServerCertificate=false&useSSL=false");
        dataSource.setUsername(mySQLContainer.getUsername());
        dataSource.setPassword(mySQLContainer.getPassword());
        return dataSource;
    }

    @Bean(name = "testDataSourceEntityManagerFactory")
    public EntityManagerFactory testDataSourceEntityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.example.unittestwithcontainer.dto");
        factory.setDataSource(dataSource);

        Map<String, String> props = new HashMap<>();
        props.put("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
        props.put("hibernate.format_sql", "true");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "create");
        factory.setJpaPropertyMap(props);

        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "testDataSourceTransactionManager")
    public PlatformTransactionManager testDataSourceTransactionManager(EntityManagerFactory testDataSourceEntityManagerFactory) {
        return new JpaTransactionManager(testDataSourceEntityManagerFactory);
    }

}
