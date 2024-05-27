package se.alpha.riskappbackend.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;


@Service
public class DatabaseService {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isClosed()) {
                logger.info("Connected to DB successfully!");
            }
        } catch (Exception e) {
            logger.error("Cannot connect to DB", e);
        }
    }
}