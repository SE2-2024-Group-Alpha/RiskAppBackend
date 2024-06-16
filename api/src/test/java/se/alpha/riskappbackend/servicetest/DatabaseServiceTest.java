package se.alpha.riskappbackend.servicetest;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.alpha.riskappbackend.service.DatabaseService;

@ExtendWith(MockitoExtension.class)
public class DatabaseServiceTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @InjectMocks
    private DatabaseService databaseService;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    @BeforeEach
    public void setUp() throws SQLException {

        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void testInitConnectionSuccess() throws SQLException {

        when(connection.isClosed()).thenReturn(false);

        // Call the method to test
        databaseService.init();

    }

    @Test
    public void testInitConnectionFailure() throws SQLException {

        when(dataSource.getConnection()).thenThrow(new SQLException("Cannot connect to DB"));


        databaseService.init();


    }
}
