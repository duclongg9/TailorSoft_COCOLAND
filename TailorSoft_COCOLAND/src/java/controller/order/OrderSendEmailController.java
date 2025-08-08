package controller.order;

import dao.customer.CustomerDAO;
import dao.order.OrderDAO;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import model.Order;
import model.OrderDetail;
import service.NotificationService;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Gửi lại thông tin đơn hàng cho khách (email + ZNS) theo yêu cầu.
 */

public class OrderSendEmailController extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(OrderSendEmailController.class.getName());

    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Customer customer = customerDAO.findById(order.getCustomerId());
        List<OrderDetail> details = orderDAO.findDetailsByOrder(orderId);
        new NotificationService().notifyOrder(customer, order, details);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}