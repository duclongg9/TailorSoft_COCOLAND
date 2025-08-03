package controller.order;

import dao.order.OrderDAO;
import dao.customer.CustomerDAO;
import dao.producttype.ProductTypeDAO;
import dao.measurement.MeasurementDAO;
import dao.material.MaterialDAO;
import model.Order;
import model.OrderDetail;
import model.Measurement;
import model.ProductType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCreateController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();
    private final MeasurementDAO measurementDAO = new MeasurementDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("customers", customerDAO.findAll());
        request.setAttribute("productTypes", productTypeDAO.findAll());
        request.setAttribute("materials", materialDAO.findAll());
        request.getRequestDispatcher("/jsp/order/createOrder.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            Date orderDate = sdf.parse(request.getParameter("orderDate"));
            Date deliveryDate = sdf.parse(request.getParameter("deliveryDate"));
            String status = request.getParameter("status");
            double total = Double.parseDouble(request.getParameter("total"));
            double deposit = Double.parseDouble(request.getParameter("deposit"));
            Order order = new Order(0, customerId, orderDate, deliveryDate, status, total, deposit);
            int orderId = orderDAO.insert(order);

            java.util.Map<String, String[]> params = request.getParameterMap();
            params.keySet().stream()
                    .filter(p -> p.startsWith("productTypeId_"))
                    .forEach(p -> {
                        String idx = p.substring("productTypeId_".length());
                        int ptId = Integer.parseInt(request.getParameter(p));
                        int qty = Integer.parseInt(request.getParameter("quantity_" + idx));
                        ProductType pt = productTypeDAO.findById(ptId);
                        OrderDetail detail = new OrderDetail();
                        detail.setOrderId(orderId);
                        detail.setProductType(pt != null ? pt.getName() : "");
                        detail.setQuantity(qty);
                        orderDAO.insertDetail(detail);

                        String prefix = "item" + idx + "_m";
                    params.keySet().stream()
                                .filter(k -> k.startsWith(prefix))
                                .forEach(k -> {
                                    int mtId = Integer.parseInt(k.substring(prefix.length()));
                                    double value = Double.parseDouble(request.getParameter(k));
                                    Measurement m = new Measurement();
                                    m.setCustomerId(customerId);
                                    m.setProductTypeId(ptId);
                                    m.setMeasurementTypeId(mtId);
                                    m.setValue(value);
                                    measurementDAO.insert(m);
                                });
                    });

            params.keySet().stream()
                    .filter(p -> p.startsWith("materialId_"))
                    .forEach(p -> {
                        String idx = p.substring("materialId_".length());
                        int mId = Integer.parseInt(request.getParameter(p));
                        double used = Double.parseDouble(request.getParameter("materialQty_" + idx));
                        materialDAO.decreaseQuantity(mId, used);
                    });

            response.sendRedirect(request.getContextPath() + "/orders?msg=created");
        } catch (ParseException | NumberFormatException e) {
            throw new ServletException(e);
        }
    }
}
