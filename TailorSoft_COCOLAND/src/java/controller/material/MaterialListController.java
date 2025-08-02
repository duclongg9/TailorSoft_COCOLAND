package controller.material;

import dao.material.MaterialDAO;
import model.Material;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MaterialListController extends HttpServlet {
    private final MaterialDAO materialDAO = new MaterialDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Material> materials = materialDAO.findAll();
        request.setAttribute("materials", materials);
        String msg = request.getParameter("msg");
        if (msg != null) {
            request.setAttribute("msg", msg);
        }
        request.getRequestDispatcher("/jsp/material/listMaterial.jsp").forward(request, response);
    }
}
