package com.tms.appconductor.shared.infrastructure.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Slf4j
@Configuration
@EnableR2dbcRepositories(basePackages = "com.tms.appconductor")
public class R2dbcConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.host:localhost}")
    private String host;

    @Value("${spring.r2dbc.port:1433}")
    private int port;

    @Value("${spring.r2dbc.database:TransporteCargaPesada}")
    private String database;

    @Value("${spring.r2dbc.username:usr-desarrollo}")
    private String username;

    @Value("${spring.r2dbc.password:123456}")
    private String password;

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        log.info("Configurando R2DBC: {}:{}/{}", host, port, database);

        ConnectionFactoryOptions options = ConnectionFactoryOptions.builder()
                .option(DRIVER, "sqlserver")
                .option(HOST, host)
                .option(PORT, port)
                .option(DATABASE, database)
                .option(USER, username)
                .option(PASSWORD, password)
                .option(Option.valueOf("trustServerCertificate"), true)
                .option(Option.valueOf("encrypt"), false)
                .option(Option.valueOf("preferCursoredExecution"), false)
                .build();

        log.info("Opciones: {}", options);
        return ConnectionFactories.get(options);
    }

    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}