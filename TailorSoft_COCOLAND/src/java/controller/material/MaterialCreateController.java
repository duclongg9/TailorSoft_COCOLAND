package controller.material;

import dao.material.MaterialDAO;
import model.Material;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@MultipartConfig
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

        Part invoicePart = request.getPart("invoiceImage");
        String fileName = null;
        if (invoicePart != null && invoicePart.getSize() > 0) {
            String original = Path.of(invoicePart.getSubmittedFileName()).getFileName().toString();
            String prefix = String.valueOf(System.currentTimeMillis());
            fileName = prefix + "_" + original;
            String uploadDir = request.getServletContext().getRealPath("/uploads");
            Files.createDirectories(Path.of(uploadDir));
            invoicePart.write(uploadDir + File.separator + fileName);
        }

        Material m = new Material(0, name, color, origin, price, quantity, fileName);
        materialDAO.insert(m);
        response.sendRedirect(request.getContextPath() + "/materials?msg=created");
    }
}
