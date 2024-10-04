package Servlet;

import DAO.CustomerDAO;
import Utils.HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		String json = HttpUtil.getRawJson(request);
		JSONObject jsonObject = new JSONObject(json);

		String email = jsonObject.getString("email");
		String password = jsonObject.getString("password");

		CustomerDAO customerDAO = new CustomerDAO();
		boolean isAuthenticated = customerDAO.authenticateUser(email, password);

		PrintWriter out = response.getWriter();
		if (isAuthenticated) {
			String customerId = customerDAO.findByEmail(email).getCustomerId();
			String result = String.format("{\"status\":\"success for customer: %s\"}", customerId);
			out.print(result);
		} else {
			out.print("{\"status\":\"Incorrect username or password\"}");
		}
		out.flush();
	}
}