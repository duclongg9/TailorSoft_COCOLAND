package controller.order;

import dao.customer.CustomerDAO;
import dao.order.OrderDAO;
import model.Customer;
import model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderHistoryController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int customerId = Integer.parseInt(idStr);
        Customer customer = customerDAO.findById(customerId);
        if (customer == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<Order> orders = orderDAO.findByCustomer(customerId);
        int total = orders.size();
        long pending = orders.stream()
                .filter(o -> o.getStatus() == null || !"Hoàn thành".equalsIgnoreCase(o.getStatus()))
                .count();
        request.setAttribute("customer", customer);
        request.setAttribute("orders", orders);
        request.setAttribute("totalOrders", total);
        request.setAttribute("pendingOrders", pending);
        request.setAttribute("completedOrders", total - pending);
        request.getRequestDispatcher("/jsp/order/customerOrderHistory.jsp").forward(request, response);
    }
}
