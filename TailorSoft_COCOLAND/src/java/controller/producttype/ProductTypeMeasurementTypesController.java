package controller.producttype;

import dao.producttype.ProductTypeDAO;
import model.MeasurementType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductTypeMeasurementTypesController extends HttpServlet {
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String ptParam = request.getParameter("productTypeId");
        if (ptParam == null || ptParam.isBlank()) {
            response.getWriter().write("[]");
            return;
        }
        try {
            int ptId = Integer.parseInt(ptParam);
            List<MeasurementType> mts = productTypeDAO.findMeasurementTypes(ptId);
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < mts.size(); i++) {
                MeasurementType mt = mts.get(i);
                sb.append("{\"id\":").append(mt.getId())
                  .append(",\"name\":\"").append(escape(mt.getName()))
                  .append("\",\"unit\":\"").append(escape(mt.getUnit()))
                  .append("\"}");
                if (i < mts.size() - 1) sb.append(',');
            }
            sb.append(']');
            response.getWriter().write(sb.toString());
        } catch (NumberFormatException e) {
            response.getWriter().write("[]");
        }
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
