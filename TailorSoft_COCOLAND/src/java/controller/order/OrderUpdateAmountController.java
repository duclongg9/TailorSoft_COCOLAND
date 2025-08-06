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
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderUpdateAmountController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderUpdateAmountController.class.getName());
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        double total = Double.parseDouble(req.getParameter("total"));
        double deposit = Double.parseDouble(req.getParameter("deposit"));

        Order order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (deposit > total) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            orderDAO.updateAmounts(orderId, total, deposit);

            if (deposit >= total) {
                order.setTotal(total);
                order.setDeposit(deposit);
                try {
                    Customer customer = customerDAO.findById(order.getCustomerId());
                    List<OrderDetail> details = orderDAO.findDetailsByOrder(orderId);
                    notificationService.sendOrderEmail(customer, order, details);
                    notificationService.sendOrderZns(order.getCustomerPhone(), order, details);
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Send notification failed", e);
                }
            }

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}

