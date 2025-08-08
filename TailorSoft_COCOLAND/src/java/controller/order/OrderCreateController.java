package controller.order;

import dao.connect.DBConnect;
import dao.customer.CustomerDAO;
import dao.material.MaterialDAO;
import dao.measurement.MeasurementDAO;
import dao.order.OrderDAO;
import dao.producttype.ProductTypeDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.*;
import service.NotificationService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tạo mới đơn hàng và gửi thông báo cho khách hàng.
 */
public class OrderCreateController extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(OrderCreateController.class.getName());
    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
    static final int MAX_ITEMS = 100;
    static final int MAX_MATERIAL_TYPES = 50;

    private final CustomerDAO customerDAO = new CustomerDAO();
    private final ProductTypeDAO productTypeDAO = new ProductTypeDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();

    // ---------- GET ---------- //
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("customers", customerDAO.findAll());
        req.setAttribute("productTypes", productTypeDAO.findAll());
        req.setAttribute("materials", materialDAO.findAll());
        req.getRequestDispatcher("/jsp/order/createOrder.jsp").forward(req, resp);
    }

    // ---------- POST ---------- //
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);
            try {
                OrderDAO orderDAO = new OrderDAO(conn);
                MeasurementDAO measurementDAO = new MeasurementDAO(conn);
                MaterialDAO connMaterialDAO = new MaterialDAO(conn);

                int customerId = intParam(req, "customerId");
                Customer customer = customerDAO.findById(customerId);

                Order order = new Order(
                        0,
                        customerId,
                        dateParam(req, "orderDate"),
                        dateParam(req, "deliveryDate"),
                        req.getParameter("status"),
                        doubleParam(req, "total"),
                        doubleParam(req, "deposit"));

                if (order.getDeposit() > order.getTotal()) {
                    throw new IllegalArgumentException("Deposit cannot exceed total.");
                }

                order.setId(orderDAO.insert(order));
                buildOrderDetails(req, orderDAO, measurementDAO, connMaterialDAO, order);

                conn.commit();

                List<OrderDetail> details = orderDAO.findDetailsByOrder(order.getId());
                new NotificationService().notifyOrder(customer, order, details);

                resp.sendRedirect(req.getContextPath() + "/orders?msg=created");
            } catch (Exception ex) {
                conn.rollback();
                LOG.log(Level.SEVERE, "Create order failed", ex);
                throw ex;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // ---------- Helpers ---------- //
    void buildOrderDetails(HttpServletRequest req,
                                   OrderDAO orderDAO,
                                   MeasurementDAO measurementDAO,
                                   MaterialDAO matDAO,
                                   Order order) throws SQLException {
        Map<String, String[]> params = req.getParameterMap();
        int itemCount = 0;
        Set<Integer> materialIds = new HashSet<>();
        for (String key : params.keySet()) {
            if (!key.startsWith("productTypeId")) continue;
            itemCount++;
            if (itemCount > MAX_ITEMS) {
                throw new IllegalArgumentException("Too many products in a single order");
            }
            String idx = key.substring("productTypeId".length());

            int ptId = intParam(req, key);
            int qty = intParam(req, "quantity" + idx);

            int materialId = optionalInt(req, "materialId_" + idx, 0);
            if (materialId > 0) {
                materialIds.add(materialId);
                if (materialIds.size() > MAX_MATERIAL_TYPES) {
                    throw new IllegalArgumentException("Too many fabric types in a single order");
                }
            }
            double usedQty = optionalDouble(req, "materialQty_" + idx, 0);
            Material material = materialId > 0 ? matDAO.findById(materialId) : null;

            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getId());
            detail.setProductTypeId(ptId);
            detail.setProductType(productTypeDAO.cacheFindName(ptId));
            detail.setQuantity(qty);
            detail.setMaterialId(materialId);
            detail.setMaterialName(material != null ? material.getName() : "");
            detail.setUnitPrice(material != null ? material.getPrice() : 0);
            detail.setNote(req.getParameter("note" + idx));

            int detailId = orderDAO.insertDetail(detail);
            if (material != null && usedQty > 0) {
                matDAO.decreaseQuantity(materialId, usedQty);
            }

            // measurements
            String prefix = "item" + idx + "_m";
            for (String p : params.keySet()) {
                if (!p.startsWith(prefix)) continue;
                int mtId = Integer.parseInt(p.substring(prefix.length()));
                double val = Double.parseDouble(req.getParameter(p));
                Measurement m = new Measurement();
                m.setCustomerId(order.getCustomerId());
                m.setProductTypeId(ptId);
                m.setMeasurementTypeId(mtId);
                m.setValue(val);
                m.setOrderDetailId(detailId);
                measurementDAO.insert(m);
            }
        }
    }

    private int intParam(HttpServletRequest req, String name) {
        return Integer.parseInt(req.getParameter(name));
    }

    private double doubleParam(HttpServletRequest req, String name) {
        return Double.parseDouble(req.getParameter(name));
    }

    private Date dateParam(HttpServletRequest req, String name) throws ParseException {
        return DF.parse(req.getParameter(name));
    }

    private int optionalInt(HttpServletRequest req, String name, int def) {
        String v = req.getParameter(name);
        return v == null || v.trim().isEmpty() ? def : Integer.parseInt(v);
    }

    private double optionalDouble(HttpServletRequest req, String name, double def) {
        String v = req.getParameter(name);
        return v == null || v.trim().isEmpty() ? def : Double.parseDouble(v);
    }
}