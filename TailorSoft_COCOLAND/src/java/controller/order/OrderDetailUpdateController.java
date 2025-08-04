package controller.order;

import dao.connect.DBConnect;
import dao.measurement.MeasurementDAO;
import dao.order.OrderDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class OrderDetailUpdateController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private final MeasurementDAO measurementDAO = new MeasurementDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int detailId = Integer.parseInt(req.getParameter("detailId"));
        int qty = Integer.parseInt(req.getParameter("quantity"));

        try (Connection c = DBConnect.getConnection()) {
            c.setAutoCommit(false);
            orderDAO.updateDetailQuantity(c, detailId, qty);
            req.getParameterMap().forEach((k, v) -> {
                if (k.startsWith("m_")) {
                    int mId = Integer.parseInt(k.substring(2));
                    double val = Double.parseDouble(v[0]);
                    try {
                        measurementDAO.updateValueById(c, mId, val);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            c.commit();
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
