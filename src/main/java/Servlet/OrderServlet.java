package Servlet;

import Utils.HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import DAO.OrderDAO;
import Model.OrderResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");

		String customerId = request.getPathInfo().replace("/", "");
		OrderDAO orderDAO = new OrderDAO();
		List<OrderResponse> orders = orderDAO.findOrdersByCustomerId(customerId);

		JSONArray result = new JSONArray();

		for (OrderResponse order : orders) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("orderId", order.getOrderId());
			jsonObject.put("items", order.getItems());
			jsonObject.put("orderStatus", order.getOrderStatus());

			result.put(jsonObject);
		}

		PrintWriter out = response.getWriter();
		out.print(result);
		out.flush();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		String json = HttpUtil.getRawJson(request);
		JSONArray jsonArray = new JSONArray(json);

		String customerId = request.getPathInfo().replace("/", "");
		OrderDAO orderDAO = new OrderDAO();
		boolean isOrderProcessed = orderDAO.processOrder(customerId, jsonArray);

		PrintWriter out = response.getWriter();
		if (isOrderProcessed) {
			out.print("{\"status\":\"success\"}");
		} else {
			out.print("{\"status\":\"failure\"}");
		}
		out.flush();
	}
}