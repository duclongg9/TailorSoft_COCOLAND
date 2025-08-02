package controller.order;

import dao.order.OrderDAO;
import dao.customer.CustomerDAO;
import model.Order;
import model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class OrderListController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Order> orders = orderDAO.findAll();
        request.setAttribute("orders", orders);
        List<Customer> customers = customerDAO.findAll();
        request.setAttribute("customers", customers);
        String msg = request.getParameter("msg");
        if (msg != null) {
            request.setAttribute("msg", msg);
        }
        request.getRequestDispatcher("/jsp/order/listOrder.jsp").forward(request, response);
    }
}
