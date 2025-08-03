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
    private static final int PAGE_SIZE = 10;

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
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }
        List<Order> allOrders = orderDAO.findByCustomer(customerId);
        int total = allOrders.size();
        long pending = allOrders.stream()
                .filter(o -> o.getStatus() == null || !"Hoàn thành".equalsIgnoreCase(o.getStatus()))
                .count();
        int from = Math.max(0, (page - 1) * PAGE_SIZE);
        int to = Math.min(from + PAGE_SIZE, total);
        List<Order> orders = allOrders.subList(from, to);
        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        request.setAttribute("customer", customer);
        request.setAttribute("orders", orders);
        request.setAttribute("totalOrders", total);
        request.setAttribute("pendingOrders", pending);
        request.setAttribute("completedOrders", total - pending);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/jsp/order/customerOrderHistory.jsp").forward(request, response);
    }
}
