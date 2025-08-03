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
    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }
        List<ProductType> all = productTypeDAO.findAll();
        int total = all.size();
        int from = Math.max(0, (page - 1) * PAGE_SIZE);
        int to = Math.min(from + PAGE_SIZE, total);
        List<ProductType> list = all.subList(from, to);
        int totalPages = (int) Math.ceil(total / (double) PAGE_SIZE);
        Map<Integer, List<MeasurementType>> map = new HashMap<>();
        for (ProductType pt : list) {
            map.put(pt.getId(), productTypeDAO.findMeasurementTypes(pt.getId()));
        }
        request.setAttribute("productTypes", list);
        request.setAttribute("productMeasurements", map);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/jsp/producttype/listProductType.jsp").forward(request, response);
    }
}
