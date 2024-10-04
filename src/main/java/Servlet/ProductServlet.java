package Servlet;

import DAO.ProductDAO;
import Model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		ProductDAO productDAO = new ProductDAO();
		List<Product> products = productDAO.getAllProducts();

		JSONArray jsonArray = new JSONArray();

		for (Product p : products) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("productId", p.getProductId());
			jsonObject.put("productName", p.getProductName());
			jsonObject.put("productDescription", p.getProductDescription());
			jsonObject.put("price", p.getPrice());
			jsonObject.put("quantity", p.getQuantity());
			jsonObject.put("category", p.getCategory());

			jsonArray.put(jsonObject);
		}

		PrintWriter out = response.getWriter();
		out.print(jsonArray);
		out.flush();
	}
}