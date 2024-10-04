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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		String json = HttpUtil.getRawJson(request);
		JSONObject jsonObject = new JSONObject(json);

		String firstName = jsonObject.getString("firstName");
		String lastName = jsonObject.getString("lastName");
		String email = jsonObject.getString("email");
		String password = jsonObject.getString("password");
		String address = jsonObject.getString("address");

		CustomerDAO customerDAO = new CustomerDAO();
		boolean isRegistered = customerDAO.registerUser(firstName, lastName, email, password, address);

		PrintWriter out = response.getWriter();
		if (isRegistered) {
			out.print("{\"status\":\"success\"}");
		} else {
			out.print("{\"status\":\"failure\"}");
		}
		out.flush();
	}
}