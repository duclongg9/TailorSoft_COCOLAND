package controller.order;

import dao.order.OrderDAO;
import model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderDetailController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // For simplicity, reuse DAO findAll() and filter
        int id = Integer.parseInt(idStr);
        Order order = orderDAO.findAll().stream().filter(o -> o.getId() == id).findFirst().orElse(null);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        request.setAttribute("order", order);
        request.getRequestDispatcher("/jsp/order/orderDetail.jsp").forward(request, response);
    }
}
