package controller.order;

import com.google.gson.Gson;
import dao.order.OrderDAO;
import dao.measurement.MeasurementDAO;
import model.Order;
import model.OrderDetail;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(idStr);
        Order order = orderDAO.findById(id);
        if (order == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<OrderDetail> details = orderDAO.findDetailsByOrder(id);

        MeasurementDAO mDao = new MeasurementDAO();
        Map<Integer, List<Map<String, Object>>> msMap = new HashMap<>();
        for (OrderDetail d : details) {
            msMap.put(d.getId(), mDao.findByOrderDetail(d.getId()));
        }

        String msJson = new Gson().toJson(msMap);

        request.setAttribute("order", order);
        request.setAttribute("details", details);
        request.setAttribute("measurementsJson", msJson);
        request.getRequestDispatcher("/jsp/order/orderDetail.jsp").forward(request, response);
    }
}
