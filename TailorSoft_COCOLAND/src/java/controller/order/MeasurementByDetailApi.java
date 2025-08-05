package controller.order;

import com.google.gson.Gson;
import dao.measurement.MeasurementDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MeasurementByDetailApi extends HttpServlet {
    private final MeasurementDAO dao = new MeasurementDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException ex) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        List<Map<String, Object>> list = dao.findByOrderDetail(id);
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().print(new Gson().toJson(list));
    }
}
