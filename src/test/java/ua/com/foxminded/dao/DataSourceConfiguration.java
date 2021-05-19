package ua.com.foxminded.dao;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;

@Profile("test")
@TestConfiguration
public class DataSourceConfiguration {

    @Autowired
    private Environment environment;

    private static final String URL = "url";
    private static final String USER = "dbuser";
    private static final String DRIVER = "driver";
    private static final String PASSWORD = "dbpassword";

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty(URL));
        driverManagerDataSource.setUsername(environment.getProperty(USER));
        driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
        driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
        return driverManagerDataSource;
    }

    @Bean
    public H2DataTypeFactory h2DataTypeFactory() {
        return new H2DataTypeFactory();
    }

    @Bean
    public DatabaseConfigBean dbUnitDatabaseConfig() {
        DatabaseConfigBean bean = new DatabaseConfigBean();
        bean.setDatatypeFactory(h2DataTypeFactory());
        return bean;
    }

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
        DatabaseDataSourceConnectionFactoryBean bean = new DatabaseDataSourceConnectionFactoryBean();
        bean.setDataSource(dataSource());
        bean.setDatabaseConfig(dbUnitDatabaseConfig());
        return bean;
    }
}
