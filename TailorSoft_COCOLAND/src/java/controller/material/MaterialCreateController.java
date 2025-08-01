package controller.material;

import dao.material.MaterialDAO;
import model.Material;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MaterialCreateController extends HttpServlet {
    private final MaterialDAO materialDAO = new MaterialDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/material/createMaterial.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String color = request.getParameter("color");
        String origin = request.getParameter("origin");
        double price = Double.parseDouble(request.getParameter("price"));
        double quantity = Double.parseDouble(request.getParameter("quantity"));
        Material m = new Material(0, name, color, origin, price, quantity);
        materialDAO.insert(m);
        response.sendRedirect(request.getContextPath() + "/materials");
    }
}
