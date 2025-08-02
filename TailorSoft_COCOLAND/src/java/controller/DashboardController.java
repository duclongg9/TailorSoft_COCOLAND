package controller;

import dao.customer.CustomerDAO;
import dao.material.MaterialDAO;
import dao.order.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DashboardController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomerDAO cDao = new CustomerDAO();
        OrderDAO oDao = new OrderDAO();
        MaterialDAO mDao = new MaterialDAO();

        int customerCount = cDao.findAll().size();
        int orderCount = oDao.findAll().size();
        long lowStockCount = mDao.findAll().stream().filter(m -> m.getQuantity() < 10).count();

        req.setAttribute("customerCount", customerCount);
        req.setAttribute("orderCount", orderCount);
        req.setAttribute("lowStockCount", lowStockCount);
        req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
    }
}
