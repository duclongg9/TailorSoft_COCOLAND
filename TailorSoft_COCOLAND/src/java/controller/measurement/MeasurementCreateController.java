package controller.measurement;

import dao.measurement.MeasurementDAO;
import dao.producttype.ProductTypeDAO;
import model.Measurement;
import model.ProductType;
import model.MeasurementType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MeasurementCreateController extends HttpServlet {
    private final MeasurementDAO measurementDAO = new MeasurementDAO();
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductType> pts = productTypeDAO.findAll();
        request.setAttribute("productTypes", pts);
        request.getRequestDispatcher("/jsp/measurement/createMeasurement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        int productTypeId = Integer.parseInt(request.getParameter("productTypeId"));
        List<MeasurementType> mts = productTypeDAO.findMeasurementTypes(productTypeId);
        for (MeasurementType mt : mts) {
            String valStr = request.getParameter("value_" + mt.getId());
            if (valStr != null && !valStr.isBlank()) {
                try {
                    double value = Double.parseDouble(valStr);
                    String note = request.getParameter("note_" + mt.getId());
                    Measurement m = new Measurement();
                    m.setCustomerId(customerId);
                    m.setProductTypeId(productTypeId);
                    m.setMeasurementTypeId(mt.getId());
                    m.setValue(value);
                    m.setNote(note);
                    measurementDAO.insert(m);
                } catch (NumberFormatException ignored) {}
            }
        }
        response.sendRedirect(request.getContextPath() + "/measurements?msg=created");
    }
}
