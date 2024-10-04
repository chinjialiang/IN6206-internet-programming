package Servlet;

import DAO.FeedbackDAO;
import Utils.HttpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/feedback")
public class FeedbackServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		String json = HttpUtil.getRawJson(request);
		JSONObject jsonObject = new JSONObject(json);

		String customerId = jsonObject.getString("customerId");
		String description = jsonObject.getString("description");

		FeedbackDAO feedbackDAO = new FeedbackDAO();
		boolean isCreated = feedbackDAO.createFeedback(customerId, description);

		PrintWriter out = response.getWriter();
		if (isCreated) {
			out.print("{\"status\":\"Success\"}");
		} else {
			out.print("{\"status\":\"Unable to create feedback\"}");
		}
		out.flush();
	}
}