package controller.measurementtype;

import dao.measurementtype.MeasurementTypeDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MeasurementTypeDeleteController extends HttpServlet {
    private final MeasurementTypeDAO measurementTypeDAO = new MeasurementTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            measurementTypeDAO.delete(id);
        }
        response.sendRedirect(request.getContextPath() + "/measurement-types");
    }
}
