package controller.order;

import dao.customer.CustomerDAO;
import dao.order.OrderDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer;
import model.Order;
import model.OrderDetail;
import service.NotificationService;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Chuyển trạng thái đơn hàng và gửi thông báo khi hoàn thành.
 */
@WebServlet("/orders/toggle-status")
public class OrderToggleStatusController extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(OrderToggleStatusController.class.getName());

    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Order order = orderDAO.findById(id);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String next = "Dang may".equals(order.getStatus()) ? "Hoan thanh" : "Dang may";
        orderDAO.updateStatus(id, next);
        order.setStatus(next);

        if ("Hoan thanh".equals(next)) {
            try {
                Customer customer = customerDAO.findById(order.getCustomerId());
                List<OrderDetail> details = orderDAO.findDetailsByOrder(id);
                new NotificationService().notifyOrder(customer, order, details);
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Send notification failed", e);
            }
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("{\"status\":\"" + next + "\"}");
    }
}