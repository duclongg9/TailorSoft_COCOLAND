package controller.order;

import dao.connect.DBConnect;
import dao.order.OrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderCancelController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        try (Connection c = DBConnect.getConnection()) {
            OrderDAO dao = new OrderDAO(c);
            dao.updateStatus(id, "Don huy");
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
        resp.sendRedirect(req.getContextPath() + "/orders?msg=cancelled");
    }
}
