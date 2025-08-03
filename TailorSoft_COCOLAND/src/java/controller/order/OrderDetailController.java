package controller.order;

import dao.order.OrderDAO;
import model.Order;
import model.OrderDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderDetailController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(idStr);
        Order order = orderDAO.findById(id);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<OrderDetail> details = orderDAO.findDetailsByOrder(id);
        request.setAttribute("order", order);
        request.setAttribute("details", details);
        request.getRequestDispatcher("/jsp/order/orderDetail.jsp").forward(request, response);
    }
}
