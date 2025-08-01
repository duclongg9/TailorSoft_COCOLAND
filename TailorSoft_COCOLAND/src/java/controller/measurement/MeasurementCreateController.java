package controller.measurement;

import dao.measurement.MeasurementDAO;
import model.Measurement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MeasurementCreateController extends HttpServlet {
    private final MeasurementDAO measurementDAO = new MeasurementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/measurement/createMeasurement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        int productTypeId = Integer.parseInt(request.getParameter("productTypeId"));
        int measurementTypeId = Integer.parseInt(request.getParameter("measurementTypeId"));
        double value = Double.parseDouble(request.getParameter("value"));
        String note = request.getParameter("note");
        Measurement m = new Measurement(0, customerId, productTypeId, measurementTypeId, value, note);
        measurementDAO.insert(m);
        response.sendRedirect(request.getContextPath() + "/measurements");
    }
}
