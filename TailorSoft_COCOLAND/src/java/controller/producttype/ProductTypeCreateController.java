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

public class ProductTypeCreateController extends HttpServlet {
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();
    private final MeasurementTypeDAO measurementTypeDAO = new MeasurementTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MeasurementType> mts = measurementTypeDAO.search(null, null);
        request.setAttribute("measurementTypes", mts);
        request.setAttribute("bodyParts", measurementTypeDAO.findBodyParts());
        request.getRequestDispatcher("/jsp/producttype/createProductType.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        pt.setName(name);
        pt.setCode(code);
        productTypeDAO.insert(pt, ids);
        response.sendRedirect(request.getContextPath() + "/product-types");
    }
}
