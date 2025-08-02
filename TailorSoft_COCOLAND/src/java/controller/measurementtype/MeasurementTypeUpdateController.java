package controller.measurementtype;

import dao.measurementtype.MeasurementTypeDAO;
import model.MeasurementType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MeasurementTypeUpdateController extends HttpServlet {
    private final MeasurementTypeDAO measurementTypeDAO = new MeasurementTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            int id = Integer.parseInt(idStr);
            MeasurementType mt = measurementTypeDAO.findById(id);
            request.setAttribute("measurementType", mt);
            request.getRequestDispatcher("/jsp/measurementtype/updateMeasurementType.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/measurement-types");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String unit = request.getParameter("unit");
        String bodyPart = request.getParameter("bodyPart");
        String note = request.getParameter("note");
        MeasurementType mt = new MeasurementType();
        mt.setId(id);
        mt.setName(name);
        mt.setUnit(unit);
        mt.setBodyPart(bodyPart);
        mt.setNote(note);
        measurementTypeDAO.update(mt);
        response.sendRedirect(request.getContextPath() + "/measurement-types");
    }
}
