package controller.order;

import dao.material.MaterialDAO;
import dao.measurement.MeasurementDAO;
import dao.order.OrderDAO;
import dao.producttype.ProductTypeDAO;
import jakarta.servlet.http.HttpServletRequest;
import model.Measurement;
import model.Material;
import model.Order;
import model.OrderDetail;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.*;

public class OrderCreateControllerTest {
    public static void main(String[] args) throws Exception {
        testTooManyProducts();
        testTooManyMaterialTypes();
        System.out.println("Tests completed");
    }

    static void testTooManyProducts() throws Exception {
        OrderCreateController ctrl = new OrderCreateController();
        Field f = OrderCreateController.class.getDeclaredField("productTypeDAO");
        f.setAccessible(true);
        f.set(ctrl, new StubProductTypeDAO());
        Order order = new Order();
        order.setId(1);
        order.setCustomerId(1);

        Map<String, String[]> params = new HashMap<>();
        for (int i = 1; i <= OrderCreateController.MAX_ITEMS + 1; i++) {
            params.put("productTypeId" + i, new String[]{"1"});
            params.put("quantity" + i, new String[]{"1"});
        }
        HttpServletRequest req = mockRequest(params);
        try {
            ctrl.buildOrderDetails(req, new StubOrderDAO(), new StubMeasurementDAO(), new StubMaterialDAO(), order);
            System.out.println("testTooManyProducts FAILED");
        } catch (IllegalArgumentException ex) {
            System.out.println("testTooManyProducts PASSED");
        }
    }

    static void testTooManyMaterialTypes() throws Exception {
        OrderCreateController ctrl = new OrderCreateController();
        Field f = OrderCreateController.class.getDeclaredField("productTypeDAO");
        f.setAccessible(true);
        f.set(ctrl, new StubProductTypeDAO());
        Order order = new Order();
        order.setId(1);
        order.setCustomerId(1);

        Map<String, String[]> params = new HashMap<>();
        int limit = OrderCreateController.MAX_MATERIAL_TYPES + 1;
        for (int i = 1; i <= limit; i++) {
            params.put("productTypeId" + i, new String[]{"1"});
            params.put("quantity" + i, new String[]{"1"});
            params.put("materialId_" + i, new String[]{String.valueOf(i)});
        }
        HttpServletRequest req = mockRequest(params);
        try {
            ctrl.buildOrderDetails(req, new StubOrderDAO(), new StubMeasurementDAO(), new StubMaterialDAO(), order);
            System.out.println("testTooManyMaterialTypes FAILED");
        } catch (IllegalArgumentException ex) {
            System.out.println("testTooManyMaterialTypes PASSED");
        }
    }

    static HttpServletRequest mockRequest(Map<String, String[]> params) {
        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class[]{HttpServletRequest.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("getParameterMap")) {
                        return params;
                    }
                    if (method.getName().equals("getParameter")) {
                        String[] v = params.get((String) args[0]);
                        return v != null && v.length > 0 ? v[0] : null;
                    }
                    return null;
                });
    }

    static class StubOrderDAO extends OrderDAO {
        public StubOrderDAO() { super(null); }
        @Override
        public int insertDetail(OrderDetail detail) throws SQLException { return 0; }
    }

    static class StubMeasurementDAO extends MeasurementDAO {
        public StubMeasurementDAO() { super(null); }
        @Override
        public void insert(Measurement m) {}
    }

    static class StubMaterialDAO extends MaterialDAO {
        public StubMaterialDAO() { super(null); }
        @Override
        public Material findById(int id) { return null; }
        @Override
        public void decreaseQuantity(int id, double amount) {}
    }

    static class StubProductTypeDAO extends ProductTypeDAO {
        @Override
        public String cacheFindName(int id) {
            return "PT";
        }
    }
}
