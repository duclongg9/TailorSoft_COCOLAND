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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Measurement> list = measurementDAO.findAll();
        request.setAttribute("measurements", list);
        request.getRequestDispatcher("/jsp/measurement/listMeasurement.jsp").forward(request, response);
    }
}
