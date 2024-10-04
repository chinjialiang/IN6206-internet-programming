package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Model.Product;
import Utils.JDBCUtilsDruid;

public class ProductDAO {

	public List<Product> getAllProducts() {

		System.out.println("Executing getAllProducts");

		String sql = "SELECT * FROM product";

		try (Connection connection = JDBCUtilsDruid.getConnection();
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(sql);) {
			List<Product> products = new ArrayList<>();

			while (rs.next()) {
				String productId = rs.getString("product_id");
				String productName = rs.getString("product_name");
				String productDescription = rs.getString("product_description");
				double price = rs.getDouble("price");
				int quantity = rs.getInt("quantity");
				String categoty = rs.getString("category");
				Product product = new Product(productId, productName, productDescription, price, quantity, categoty);
				products.add(product);
			}

			return products;

		} catch (SQLException e) {
			e.printStackTrace();
			return List.of();
		}

	}

	public Product getByProductId(String id) {

		System.out.println("Executing getByProductId for productId: " + id);

		String sql = "SELECT * from product where product_id = (?)";

		try (Connection connection = JDBCUtilsDruid.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)

		) {

			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				String productId = rs.getString("product_id");
				String productName = rs.getString("product_name");
				String productDescription = rs.getString("product_description");
				double price = rs.getDouble("price");
				int quantity = rs.getInt("quantity");
				String category = rs.getString("category");
				return new Product(productId, productName, productDescription, price, quantity, category);
			}

			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}