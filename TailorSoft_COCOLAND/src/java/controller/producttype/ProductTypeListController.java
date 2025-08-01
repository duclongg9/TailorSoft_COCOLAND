package controller.producttype;

import dao.producttype.ProductTypeDAO;
import model.ProductType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProductTypeListController extends HttpServlet {
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<ProductType> list = productTypeDAO.findAll();
        request.setAttribute("productTypes", list);
        request.getRequestDispatcher("/jsp/producttype/listProductType.jsp").forward(request, response);
    }
}
