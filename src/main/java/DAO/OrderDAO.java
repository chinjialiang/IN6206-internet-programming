package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import Model.OrderResponse;
import Model.Product;
import Utils.JDBCUtilsDruid;

public class OrderDAO {

	public boolean processOrder(String customerId, JSONArray order) {

		System.out.println("Executing processOrder for customerId: " + customerId);

		String orderId = "O" + Long.toString(System.currentTimeMillis());
		String SQL_INSERT = "INSERT INTO orders (order_id, customer_id, order_detail, order_status) VALUES (?, ?, ?, ?)";
		String SQL_UPDATE = "UPDATE product SET quantity = (?) WHERE product_id = (?)";

		List<Product> customerOrder = transformJSONArray(order);
		ProductDAO productDAO = new ProductDAO();

		if (!validateOrder(customerOrder)) {
			return false;
		}

		try (Connection connection = JDBCUtilsDruid.getConnection();
				PreparedStatement preparedStatementInsert = connection.prepareStatement(SQL_INSERT);
				PreparedStatement preparedStatementUpdate = connection.prepareStatement(SQL_UPDATE);) {

			connection.setAutoCommit(false);

			preparedStatementInsert.setString(1, orderId);
			preparedStatementInsert.setString(2, customerId);
			preparedStatementInsert.setString(3, order.toString());
			preparedStatementInsert.setString(4, "CREATED");

			int result = preparedStatementInsert.executeUpdate();

			for (Product co : customerOrder) {
				String productStockId = co.getProductId();
				int customerOrderQuantity = co.getQuantity();
				Product productStock = productDAO.getByProductId(productStockId);
				int productStockQuantity = productStock.getQuantity();

				preparedStatementUpdate.setInt(1, productStockQuantity - customerOrderQuantity);
				preparedStatementUpdate.setString(2, productStockId);
				preparedStatementUpdate.executeUpdate();
			}

			connection.commit();
			connection.setAutoCommit(true);

			return result > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<OrderResponse> findOrdersByCustomerId(String id) {

		System.out.println("Executing findOrdersByCustomerId for customerId: " + id);

		String sql = "SELECT * from orders where customer_id = (?)";

		try (Connection connection = JDBCUtilsDruid.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(sql)

		) {

			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			List<OrderResponse> orders = new ArrayList<>();

			while (rs.next()) {
				String orderId = rs.getString("order_id");
				String orderDetail = rs.getString("order_detail");
				String orderStatus = rs.getString("order_status");

				JSONArray jsonArray = new JSONArray(orderDetail);
				List<Product> customerOrder = transformJSONArray(jsonArray);

				OrderResponse orderResponse = new OrderResponse(orderId, customerOrder, orderStatus);
				orders.add(orderResponse);
			}

			return orders;

		} catch (SQLException e) {
			e.printStackTrace();
			return List.of();
		}

	}

	private boolean validateOrder(List<Product> customerOrder) {

		System.out.println("Executing validateOrder for customerOrder: " + customerOrder);

		ProductDAO productDAO = new ProductDAO();

		for (Product p : customerOrder) {
			String productId = p.getProductId();
			int orderQuantity = p.getQuantity();

			Product productStock = productDAO.getByProductId(productId);

			if (productStock == null) {
				System.out.println("[Validation Error] Invalid productId: " + productId);
				return false;
			}

			int productStockQuantity = productStock.getQuantity();

			if (orderQuantity > productStockQuantity) {
				System.out.println("[Validation Error] Order quantity for productId: " + productId
						+ " cannot be greater than " + productStockQuantity);
				return false;
			}
		}

		return true;
	}

	private List<Product> transformJSONArray(JSONArray jsonArray) {

		try {

			List<Product> listProduct = new ArrayList<>();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonProduct = jsonArray.getJSONObject(i);

				String productId = jsonProduct.getString("productId");
				String productName = jsonProduct.getString("productName");
				String productDescription = jsonProduct.getString("productDescription");
				double unitPrice = jsonProduct.getDouble("unitPrice");
				int quantity = jsonProduct.getInt("quantity");
				String category = jsonProduct.getString("category");

				Product product = new Product(productId, productName, productDescription, unitPrice, quantity,
						category);
				listProduct.add(product);
			}

			return listProduct;

		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}

	}
}