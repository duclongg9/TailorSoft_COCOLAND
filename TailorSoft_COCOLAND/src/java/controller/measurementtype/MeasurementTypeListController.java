package controller.measurementtype;

import dao.measurementtype.MeasurementTypeDAO;
import model.MeasurementType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MeasurementTypeListController extends HttpServlet {
    private final MeasurementTypeDAO measurementTypeDAO = new MeasurementTypeDAO();
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bodyPart = request.getParameter("bodyPart");
        String q = request.getParameter("q");
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }
        List<MeasurementType> all = measurementTypeDAO.search(bodyPart, q);
        int total = all.size();
        int from = Math.max(0, (page - 1) * PAGE_SIZE);
        int to = Math.min(from + PAGE_SIZE, total);
        List<MeasurementType> list = all.subList(from, to);
        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        List<String> bodyParts = measurementTypeDAO.findBodyParts();
        request.setAttribute("measurementTypes", list);
        request.setAttribute("bodyParts", bodyParts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/jsp/measurementtype/listMeasurementType.jsp").forward(request, response);
    }
}
