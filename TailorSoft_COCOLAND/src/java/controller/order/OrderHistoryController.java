package controller.order;

import dao.order.OrderDAO;
import model.OrderDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderHistoryController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int customerId = Integer.parseInt(idStr);
        List<OrderDetail> details = orderDAO.findDetailsByCustomer(customerId);
        request.setAttribute("details", details);
        request.getRequestDispatcher("/jsp/order/customerOrderHistory.jsp").forward(request, response);
    }
}
