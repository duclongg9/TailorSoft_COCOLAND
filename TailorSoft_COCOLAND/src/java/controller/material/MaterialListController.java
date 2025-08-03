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
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }
        List<Material> all = materialDAO.findAll();
        int total = all.size();
        int from = Math.max(0, (page - 1) * PAGE_SIZE);
        int to = Math.min(from + PAGE_SIZE, total);
        List<Material> materials = all.subList(from, to);
        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        request.setAttribute("materials", materials);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        String msg = request.getParameter("msg");
        if (msg != null) {
            request.setAttribute("msg", msg);
        }
        request.getRequestDispatcher("/jsp/material/listMaterial.jsp").forward(request, response);
    }
}
