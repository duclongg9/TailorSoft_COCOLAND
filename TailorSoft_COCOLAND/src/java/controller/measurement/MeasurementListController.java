package controller.measurement;

import dao.measurement.MeasurementDAO;
import model.Measurement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MeasurementListController extends HttpServlet {
    private final MeasurementDAO measurementDAO = new MeasurementDAO();
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
        List<Measurement> all = measurementDAO.findAll();
        int total = all.size();
        int from = Math.max(0, (page - 1) * PAGE_SIZE);
        int to = Math.min(from + PAGE_SIZE, total);
        List<Measurement> list = all.subList(from, to);
        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        request.setAttribute("measurements", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/jsp/measurement/listMeasurement.jsp").forward(request, response);
    }
}
