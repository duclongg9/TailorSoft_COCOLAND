package controller.order;

import dao.order.OrderDAO;
import dao.customer.CustomerDAO;
import model.Order;
import model.OrderDetail;
import model.Customer;
import service.NotificationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for sending order information via email and ZNS on demand.
 */
public class OrderSendEmailController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderSendEmailController.class.getName());
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        try {
            Customer customer = customerDAO.findById(order.getCustomerId());
            List<OrderDetail> details = orderDAO.findDetailsByOrder(orderId);
            boolean emailSent = notificationService.sendOrderEmail(customer, order, details);
            notificationService.sendOrderZns(order.getCustomerPhone(), order, details);
            resp.setStatus(emailSent ? HttpServletResponse.SC_OK : HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Send notification failed", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
