package controller.order;

import dao.order.OrderDAO;
import model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@MultipartConfig
public class OrderPaymentImageUploadController extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        Order order = orderDAO.findById(orderId);
        if (order == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String type = req.getParameter("type");
        Part imagePart = req.getPart("paymentImage");
        String fileName = null;
        if (imagePart != null && imagePart.getSize() > 0) {
            String original = Path.of(imagePart.getSubmittedFileName()).getFileName().toString();
            String prefix = String.valueOf(System.currentTimeMillis());
            fileName = prefix + "_" + original;
            String uploadDir = req.getServletContext().getRealPath("/uploads");
            Files.createDirectories(Path.of(uploadDir));
            imagePart.write(uploadDir + File.separator + fileName);
        }
        if (fileName != null) {
            try {
                if ("full".equalsIgnoreCase(type)) {
                    orderDAO.updateFullImage(orderId, fileName);
                } else {
                    orderDAO.updateDepositImage(orderId, fileName);
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/orders/detail?id=" + orderId);
    }
}
