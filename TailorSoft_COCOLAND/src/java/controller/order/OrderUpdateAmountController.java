package controller.order;

import dao.customer.CustomerDAO;
import dao.order.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import model.Order;
import model.OrderDetail;
import service.NotificationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cập nhật tiền cọc/tổng tiền; gửi mail nếu đã thanh toán đủ.
 */
@WebServlet("/orders/update-amount")
public class OrderUpdateAmountController extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(OrderUpdateAmountController.class.getName());

    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        double total = Double.parseDouble(req.getParameter("total"));
        double deposit = Double.parseDouble(req.getParameter("deposit"));

        if (deposit > total) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Order order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            orderDAO.updateAmounts(orderId, total, deposit);
            order.setTotal(total);
            order.setDeposit(deposit);

            if (deposit >= total) {
                Customer customer = customerDAO.findById(order.getCustomerId());
                List<OrderDetail> details = orderDAO.findDetailsByOrder(orderId);
                new NotificationService().notifyOrder(customer, order, details);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Update amounts failed", e);
            throw new ServletException(e);
        }
    }
}