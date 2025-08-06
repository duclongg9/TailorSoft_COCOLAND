package controller.order;

import dao.order.OrderDAO;
import model.Order;
import model.OrderDetail;
import service.NotificationService;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderToggleStatusController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderToggleStatusController.class.getName());
    private final OrderDAO dao = new OrderDAO();
    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Order o = dao.findById(id);
        if (o == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String next = "Dang may".equals(o.getStatus()) ? "Hoan thanh" : "Dang may";
        dao.updateStatus(id, next);
        o.setStatus(next);

        if ("Hoan thanh".equals(next)) {
            try {
                List<OrderDetail> details = dao.findDetailsByOrder(id);
                notificationService.sendOrderEmail(o.getCustomerEmail(), o, details);
                notificationService.sendOrderZns(o.getCustomerPhone(), o, details);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Send notification failed", e);
            }
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("{\"status\":\"" + next + "\"}");
    }
}
