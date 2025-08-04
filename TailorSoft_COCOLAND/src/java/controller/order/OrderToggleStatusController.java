package controller.order;

import dao.order.OrderDAO;
import model.Order;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/orders/toggle-status")
public class OrderToggleStatusController extends HttpServlet {
    private final OrderDAO dao = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Order o = dao.findById(id);
        if (o == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String next = "Dang may".equals(o.getStatus()) ? "Hoan thanh" : "Dang may";
        dao.updateStatus(id, next);
        resp.setContentType("application/json");
        resp.getWriter().print("{\"status\":\"" + next + "\"}");
    }
}