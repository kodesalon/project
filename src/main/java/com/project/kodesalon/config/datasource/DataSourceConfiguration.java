package com.project.kodesalon.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

@Profile("prod")
@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DataSourceConfiguration {

    private static final String MASTER_DATA_SOURCE = "masterDataSource";
    private static final String SLAVE_DATA_SOURCE = "slaveDataSource";
    private static final String LAZY_DATA_SOURCE = "lazyDataSource";
    private static final String PROJECT_PACKAGE = "com.project.kodesalon";

    private final String masterKey;
    private final String slaveKey;
    private final JpaProperties jpaProperties;

    public DataSourceConfiguration(@Value("${spring.datasource.master.key}") final String masterKey,
                                   @Value("${spring.datasource.slave.key}") final String slaveKey,
                                   final JpaProperties jpaProperties) {
        this.masterKey = masterKey;
        this.slaveKey = slaveKey;
        this.jpaProperties = jpaProperties;
    }

    @Bean(MASTER_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master.hikari")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(SLAVE_DATA_SOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(LAZY_DATA_SOURCE)
    public DataSource dataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public DataSource routingDataSource(@Qualifier(MASTER_DATA_SOURCE) DataSource master,
                                        @Qualifier(SLAVE_DATA_SOURCE) DataSource slave) {
        Map<Object, Object> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put(masterKey, master);
        dataSourceMap.put(slaveKey, slave);

        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource(masterKey, slaveKey);
        replicationRoutingDataSource.setTargetDataSources(dataSourceMap);
        replicationRoutingDataSource.setDefaultTargetDataSource(master);
        return replicationRoutingDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier(LAZY_DATA_SOURCE) DataSource dataSource) {
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
        return entityManagerFactoryBuilder.dataSource(dataSource).packages(PROJECT_PACKAGE).build();
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        AbstractJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.getProperties(), null);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
