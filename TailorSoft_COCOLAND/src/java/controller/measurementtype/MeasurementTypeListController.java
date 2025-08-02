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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MeasurementType> list = measurementTypeDAO.findAll();
        request.setAttribute("measurementTypes", list);
        request.getRequestDispatcher("/jsp/measurementtype/listMeasurementType.jsp").forward(request, response);
    }
}
