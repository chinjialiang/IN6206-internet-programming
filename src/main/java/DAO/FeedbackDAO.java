package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Utils.JDBCUtilsDruid;

public class FeedbackDAO {

	public boolean createFeedback(String customerId, String description) {

		System.out.println("Executing createFeedback for customerId: " + customerId);

		String feedbackId = "F" + Long.toString(System.currentTimeMillis());
		String sql = "INSERT INTO feedback (feedback_id, customer_id, description) VALUES (?, ?, ?)";

		try (Connection connection = JDBCUtilsDruid.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, feedbackId);
			preparedStatement.setString(2, customerId);
			preparedStatement.setString(3, description);

			int result = preparedStatement.executeUpdate();
			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}