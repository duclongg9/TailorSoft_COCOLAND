package controller.order;

import dao.order.OrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class OrderUpdateAmountController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        double total = Double.parseDouble(req.getParameter("total"));
        double deposit = Double.parseDouble(req.getParameter("deposit"));
        if (deposit > total) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            orderDAO.updateAmounts(orderId, total, deposit);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
}

