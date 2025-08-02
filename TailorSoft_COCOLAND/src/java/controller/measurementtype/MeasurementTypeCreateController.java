package controller.measurementtype;

import dao.measurementtype.MeasurementTypeDAO;
import model.MeasurementType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MeasurementTypeCreateController extends HttpServlet {
    private final MeasurementTypeDAO measurementTypeDAO = new MeasurementTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/measurementtype/createMeasurementType.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String bodyPart = request.getParameter("bodyPart");
        String note = request.getParameter("note");
        MeasurementType mt = new MeasurementType();
        mt.setName(name);
        mt.setUnit(unit);
        mt.setBodyPart(bodyPart);
        mt.setNote(note);
        measurementTypeDAO.insert(mt);
        response.sendRedirect(request.getContextPath() + "/measurement-types");
    }
}
