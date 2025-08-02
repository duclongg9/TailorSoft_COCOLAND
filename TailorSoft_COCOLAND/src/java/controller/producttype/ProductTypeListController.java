package controller.producttype;

import dao.producttype.ProductTypeDAO;
import model.ProductType;
import model.MeasurementType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ProductTypeListController extends HttpServlet {
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductType> list = productTypeDAO.findAll();
        Map<Integer, List<MeasurementType>> map = new HashMap<>();
        for (ProductType pt : list) {
            map.put(pt.getId(), productTypeDAO.findMeasurementTypes(pt.getId()));
        }
        request.setAttribute("productTypes", list);
        request.setAttribute("productMeasurements", map);
        request.getRequestDispatcher("/jsp/producttype/listProductType.jsp").forward(request, response);
    }
}
