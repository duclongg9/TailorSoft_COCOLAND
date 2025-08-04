package controller.order;

import dao.order.OrderDAO;
import dao.customer.CustomerDAO;
import dao.producttype.ProductTypeDAO;
import dao.measurement.MeasurementDAO;
import dao.material.MaterialDAO;
import dao.connect.DBConnect;
import model.Order;
import model.OrderDetail;
import model.Measurement;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderCreateController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(OrderCreateController.class.getName());
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();
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
        request.setCharacterEncoding("UTF-8");
        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);
            try {
                OrderDAO orderDAO = new OrderDAO(conn);
                MeasurementDAO measurementDAO = new MeasurementDAO(conn);
                MaterialDAO mDao = new MaterialDAO(conn);

                int customerId = Integer.parseInt(request.getParameter("customerId"));
                Date orderDate = sdf.parse(request.getParameter("orderDate"));
                Date deliveryDate = sdf.parse(request.getParameter("deliveryDate"));
                String status = request.getParameter("status");
                double total = Double.parseDouble(request.getParameter("total"));
                double deposit = Double.parseDouble(request.getParameter("deposit"));
                if (deposit > total) throw new IllegalArgumentException("Deposit > Total");

                Order order = new Order(0, customerId, orderDate, deliveryDate, status, total, deposit);
                int orderId = orderDAO.insert(order);
                if (orderId < 1) throw new SQLException("Insert order failed");

                Map<String, String[]> params = request.getParameterMap();
                params.keySet().stream()
                        .filter(k -> k.startsWith("productTypeId"))
                        .forEach(k -> {
                            String idx = k.substring("productTypeId".length());
                            int ptId = Integer.parseInt(request.getParameter(k));
                            int qty = Integer.parseInt(request.getParameter("quantity" + idx));
                            String note = request.getParameter("note" + idx);

                            OrderDetail d = new OrderDetail();
                            d.setOrderId(orderId);
                            d.setProductType(productTypeDAO.cacheFindName(ptId));
                            d.setMaterialName("");
                            d.setUnitPrice(0);
                            d.setQuantity(qty);
                            d.setNote(note);
                            int detailId;
                            try {
                                detailId = orderDAO.insertDetail(d);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            String pre = "item" + idx + "_m";
                            params.keySet().stream()
                                    .filter(p -> p.startsWith(pre))
                                    .forEach(p -> {
                                        int mtId = Integer.parseInt(p.substring(pre.length()));
                                        double val = Double.parseDouble(request.getParameter(p));
                                        Measurement m = new Measurement();
                                        m.setCustomerId(customerId);
                                        m.setProductTypeId(ptId);
                                        m.setMeasurementTypeId(mtId);
                                        m.setValue(val);
                                        m.setOrderDetailId(detailId);
                                        try {
                                            measurementDAO.insert(m);
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                        });

                params.keySet().stream()
                        .filter(k -> k.startsWith("materialId_"))
                        .forEach(k -> {
                            String idx = k.substring("materialId_".length());
                            int mId = Integer.parseInt(request.getParameter(k));
                            double used = Double.parseDouble(request.getParameter("materialQty_" + idx));
                            try {
                                mDao.decreaseQuantity(mId, used);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });

                conn.commit();
                response.sendRedirect(request.getContextPath() + "/orders?msg=created");
            } catch (Exception ex) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Create order failed", ex);
                throw ex;
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }
}
