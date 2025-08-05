package controller.order;

import dao.order.OrderDAO;
import model.Order;


import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderToggleStatusController extends HttpServlet {
    private final OrderDAO dao = new OrderDAO();

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

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("{\"status\":\"" + next + "\"}");
    }
}
