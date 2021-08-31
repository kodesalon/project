package com.project.kodesalon.config;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;


@Profile("test")
@TestConfiguration
public class DBUnitTestConfiguration {

    @Bean
    public DatabaseConfigBean dbUnitDatabaseConfig() {
        DatabaseConfigBean databaseConfigBean = new DatabaseConfigBean();
        databaseConfigBean.setAllowEmptyFields(true);
        databaseConfigBean.setDatatypeFactory(new H2DataTypeFactory());
        return databaseConfigBean;
    }

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection(final DataSource dataSource) {
        DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection = new DatabaseDataSourceConnectionFactoryBean();
        dbUnitDatabaseConnection.setDataSource(dataSource);
        dbUnitDatabaseConnection.setDatabaseConfig(dbUnitDatabaseConfig());
        return dbUnitDatabaseConnection;
    }
}
