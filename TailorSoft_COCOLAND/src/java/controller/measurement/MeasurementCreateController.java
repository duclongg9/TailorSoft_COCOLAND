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
        String ptParam = request.getParameter("productTypeId");
        List<ProductType> pts = productTypeDAO.findAll();
        request.setAttribute("productTypes", pts);
        if (ptParam != null && !ptParam.isEmpty()) {
            try {
                int ptId = Integer.parseInt(ptParam);
                List<MeasurementType> mts = productTypeDAO.findMeasurementTypes(ptId);
                request.setAttribute("selectedProductType", ptId);
                request.setAttribute("measurementTypes", mts);
            } catch (NumberFormatException ignored) {}
        }
        request.getRequestDispatcher("/jsp/measurement/createMeasurement.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int customerId = Integer.parseInt(request.getParameter("customerId"));
        int productTypeId = Integer.parseInt(request.getParameter("productTypeId"));
        int measurementTypeId = Integer.parseInt(request.getParameter("measurementTypeId"));
        double value = Double.parseDouble(request.getParameter("value"));
        String note = request.getParameter("note");
        Measurement m = new Measurement();
        m.setCustomerId(customerId);
        m.setProductTypeId(productTypeId);
        m.setMeasurementTypeId(measurementTypeId);
        m.setValue(value);
        m.setNote(note);
        measurementDAO.insert(m);
        response.sendRedirect(request.getContextPath() + "/measurements");
    }
}
