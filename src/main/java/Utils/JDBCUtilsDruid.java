package Utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtilsDruid {
	private static final DataSource dataSource;

	static {
		Properties properties = new Properties();
		try (InputStream input = JDBCUtilsDruid.class.getClassLoader().getResourceAsStream("druid.properties")) {
			if (input == null) {
				throw new RuntimeException("Unable to find druid.properties");
			}
			properties.load(input);
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// getConnection
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}

	// release Connection
	public static void close(ResultSet resultSet, Statement statement, Connection connection) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}