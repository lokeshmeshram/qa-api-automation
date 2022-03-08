package com.automation.platform.shared.services.tests;

import io.qameta.allure.*;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Logger;
import static com.automation.platform.shared.services.SAASConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;

@Epic("Observability Infra Details : CPU , Memory , Disk ")
@Feature("Observability Framework feature")
public class ObservabilityInfraDetailsTest extends BaseTest {

    private final static Logger LOGGER = Logger.getLogger(ObservabilityInfraDetailsTest.class.getName());
    private static Connection con = null;
    private static Statement stmt = null;
    Properties prop = new Properties();

    @BeforeTest
    public void setup() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream rs = classLoader.getResourceAsStream(DATABASE_PKG_PATH + DATABASE_FILENAME + DATABASE_PROPERTIES_EXTENSION);
        prop.load(rs);
        String driverClassName = prop.getProperty("DB.driverClassName");
        String url = prop.getProperty("DB.url");
        String username = (!(prop.getProperty("DB.username").isEmpty()) ? prop.getProperty("DB.username") : DB_USERNAME);
        String password = (!(prop.getProperty("DB.password").isEmpty()) ? prop.getProperty("DB.password") : DB_PASSWORD);
        try {
            Class.forName(driverClassName);
            con = DriverManager
                    .getConnection(url,
                            username, password);
            con.setAutoCommit(false);
            System.out.println("Opened database successfully");
            stmt = con.createStatement();
        } catch (Exception e) {
            LOGGER.info(e.getClass().getName() + ": " + e.getMessage());
            throw new RuntimeException("BeforeTest Fail for Class : "+LOGGER.getName());
        }
    }

    @Description("Observability Framework Test for CPU Details Not Null ")
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"observabilityInfraDetailstest", "Positive"})
    public void CPUMetricShouldNotBeNull() {
        String schemaName = prop.getProperty("DB.schemaName");
        String cpuMetricsTable = prop.getProperty("DB.cpuMeticsTableName");
        String nodeReplicationCount = prop.getProperty("DB.nodeReplicationCount");
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + schemaName + "." + cpuMetricsTable + " ORDER BY time DESC LIMIT " + nodeReplicationCount);
            System.out.println("| cluster_id | node_id  |time | system_usage | user_usage | total_usage |");
            while (rs.next()) {
                String cluster_id = rs.getString("cluster_id");
                String node_id = rs.getString("node_id");
                Timestamp time = rs.getTimestamp("time");
                Double system_usage = rs.getDouble("system_usage");
                Double user_usage = rs.getDouble("user_usage");
                Double total_usage = rs.getDouble("total_usage");
                LOGGER.info("| " + cluster_id + " | " + node_id + " | " + time + " | " + system_usage + " | " + user_usage + " | " + total_usage + " |");
                LocalDateTime databaseEntryDate = time.toLocalDateTime();
                LocalDateTime currentDate = LocalDateTime.now();
                LocalDateTime databaseEntryDatePlus1Hour = databaseEntryDate.plus(Duration.ofSeconds(3600));
                LOGGER.info("databaseEntryDate =" + databaseEntryDate);
                LOGGER.info("currentDate =" + currentDate);
                LOGGER.info("databaseEntryDatePlus1Hour =" + databaseEntryDatePlus1Hour);
                assertTrue(databaseEntryDate.isBefore(currentDate));
                assertTrue(databaseEntryDatePlus1Hour.isAfter(currentDate));
                assertNotNull(cluster_id);
                assertNotNull(node_id);
                assertNotNull(system_usage);
                assertNotNull(user_usage);
                assertNotNull(total_usage);
            }
            rs.close();
        } catch (Exception e) {
            LOGGER.info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Description("Observability Framework Test for Memory Details Not Null ")
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"observabilityInfraDetailstest", "Positive"})
    public void MemoryMetricShouldNotBeNull() {
        String schemaName = prop.getProperty("DB.schemaName");
        String memoryMetricsTable = prop.getProperty("DB.memoryMeticsTableName");
        String nodeReplicationCount = prop.getProperty("DB.nodeReplicationCount");
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + schemaName + "." + memoryMetricsTable + " ORDER BY time DESC LIMIT " + nodeReplicationCount);
            LOGGER.info("| cluster_id | node_id  |time | free_mem | used_mem | total_mem |");
            while (rs.next()) {
                String cluster_id = rs.getString("cluster_id");
                String node_id = rs.getString("node_id");
                Timestamp time = rs.getTimestamp("time");
                long free_mem = rs.getLong("free_mem");
                long used_mem = rs.getLong("used_mem");
                long total_mem = rs.getLong("total_mem");
                LOGGER.info("| " + cluster_id + " | " + node_id + " | " + time + " | " + free_mem + " | " + used_mem + " | " + total_mem + " |");
                LocalDateTime databaseEntryDate = time.toLocalDateTime();
                LocalDateTime currentDate = LocalDateTime.now();
                LocalDateTime databaseEntryDatePlus1Hour = databaseEntryDate.plus(Duration.ofSeconds(3600));
                LOGGER.info("databaseEntryDate =" + databaseEntryDate);
                LOGGER.info("currentDate =" + currentDate);
                LOGGER.info("databaseEntryDatePlus1Hour =" + databaseEntryDatePlus1Hour);
                assertTrue(databaseEntryDate.isBefore(currentDate));
                assertTrue(databaseEntryDatePlus1Hour.isAfter(currentDate));
                assertNotNull(cluster_id);
                assertNotNull(node_id);
                assertNotNull(free_mem);
                assertNotNull(used_mem);
                assertNotNull(total_mem);
            }
            rs.close();
        } catch (Exception e) {
            LOGGER.info(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Step
    public void assertNotNull(String columnName) {
        assertThat("Database Column with Not Null Values : ",columnName, is(notNullValue()));
    }

    @Step
    public void assertNotNull(Double columnName) {
        assertThat("Database Column with Not Null Values : ",columnName, is(notNullValue()));
    }

    @Step
    public void assertNotNull(long columnName) {
        assertThat("Database Column with Not Null Values : ",columnName, is(notNullValue()));
    }

    @Step
    public void assertTrue(boolean value) {
        assertThat("Database Column Boolean comparison failed : ", value,equalTo(true));
    }

    @AfterTest
    public void tearDown() throws Exception {
        // Close DB statement
        if (stmt != null) {
            stmt.close();
        }
        // Close DB connection
        if (con != null) {
            con.close();
        }
    }
}
