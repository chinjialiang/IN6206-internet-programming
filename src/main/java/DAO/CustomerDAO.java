package DAO;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.google.common.hash.Hashing;

import Model.Customer;
import Utils.JDBCUtilsDruid;

public class CustomerDAO {

	public boolean registerUser(String firstName, String lastName, String email, String password, String address) {

		System.out.println("Executing registerUser for email: " + email + " password: " + password);

		String customerId = "C" + Long.toString(System.currentTimeMillis());
		String salt = UUID.randomUUID().toString();
		String hashedPassword = Hashing.sha256().hashString(salt + password, StandardCharsets.UTF_8).toString();

		if (existsByEmail(email)) {
			System.out.println("[Validation Error] Email already exists");
			return false;
		}

		String sql = "INSERT INTO customer (customer_id, first_name, last_name, email, salt, password, address) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = JDBCUtilsDruid.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, customerId);
			preparedStatement.setString(2, firstName);
			preparedStatement.setString(3, lastName);
			preparedStatement.setString(4, email);
			preparedStatement.setString(5, salt);
			preparedStatement.setString(6, hashedPassword);
			preparedStatement.setString(7, address);

			int result = preparedStatement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean authenticateUser(String email, String password) {

		System.out.println("Executing authenticateUser for email: " + email + " password: " + password);

		if (!existsByEmail(email)) {
			System.out.println("[Validation Error] Email does not exists");
			return false;
		}

		Customer customer = findByEmail(email);
		String salt = customer.getSalt();
		String hashedPassword = Hashing.sha256().hashString(salt + password, StandardCharsets.UTF_8).toString();

		if (salt != null) {
			return customer.getPassword().equals(hashedPassword);
		}

		return false;
	}

	public boolean existsByEmail(String email) {

		System.out.println("Executing existsByEmail for email: " + email);

		String sql = "SELECT * from customer where email = (?)";

		try (Connection connection = JDBCUtilsDruid.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)

		) {

			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				return true;
			}

			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Customer findByEmail(String email) {

		System.out.println("Executing findByEmail for email: " + email);

		String sql = "SELECT * from customer where email = (?)";

		try (Connection connection = JDBCUtilsDruid.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)

		) {

			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String customerId = rs.getString("customer_id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				String salt = rs.getString("salt");
				String password = rs.getString("password");
				String address = rs.getString("address");
				return new Customer(customerId, firstName, lastName, email, salt, password, address);
			}

			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
}