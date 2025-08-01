package controller.order;

import dao.order.OrderDAO;
import model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCreateController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/order/createOrder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            Date orderDate = sdf.parse(request.getParameter("orderDate"));
            Date deliveryDate = sdf.parse(request.getParameter("deliveryDate"));
            String status = request.getParameter("status");
            double total = Double.parseDouble(request.getParameter("total"));
            double deposit = Double.parseDouble(request.getParameter("deposit"));
            Order order = new Order(0, customerId, orderDate, deliveryDate, status, total, deposit);
            orderDAO.insert(order);
            response.sendRedirect(request.getContextPath() + "/orders");
        } catch (ParseException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }
}
