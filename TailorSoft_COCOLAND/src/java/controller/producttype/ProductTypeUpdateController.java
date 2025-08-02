package controller.producttype;

import dao.producttype.ProductTypeDAO;
import dao.measurementtype.MeasurementTypeDAO;
import model.ProductType;
import model.MeasurementType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProductTypeUpdateController extends HttpServlet {
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();
    private final MeasurementTypeDAO measurementTypeDAO = new MeasurementTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductType pt = productTypeDAO.findById(id);
        List<MeasurementType> mts = measurementTypeDAO.search(null, null);
        List<MeasurementType> selected = productTypeDAO.findMeasurementTypes(id);
        List<Integer> selectedIds = new ArrayList<>();
        for (MeasurementType mt : selected) {
            selectedIds.add(mt.getId());
        }
        request.setAttribute("productType", pt);
        request.setAttribute("measurementTypes", mts);
        request.setAttribute("selectedIds", selectedIds);
        request.setAttribute("bodyParts", measurementTypeDAO.findBodyParts());
        request.getRequestDispatcher("/jsp/producttype/updateProductType.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String[] mtIds = request.getParameterValues("measurementTypeId");
        List<Integer> ids = new ArrayList<>();
        if (mtIds != null) {
            for (String s : mtIds) {
                try { ids.add(Integer.parseInt(s)); } catch (NumberFormatException ignored) {}
            }
        }
        ProductType pt = new ProductType();
        pt.setId(id);
        pt.setName(name);
        pt.setCode(code);
        productTypeDAO.update(pt, ids);
        response.sendRedirect(request.getContextPath() + "/product-types");
    }
}
