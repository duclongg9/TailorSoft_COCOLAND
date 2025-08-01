package controller.customer;

import dao.customer.CustomerDAO;
import model.Customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CustomerListController extends HttpServlet {

    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Customer> customers = customerDAO.findAll();
        request.setAttribute("customers", customers);
        request.getRequestDispatcher("/jsp/customer/listCustomer.jsp").forward(request, response);
    }
}
