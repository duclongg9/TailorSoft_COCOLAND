package dao.order;

import dao.connect.DBConnect;
import model.Order;
import model.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT dh.ma_don, dh.ma_khach, kh.ho_ten, kh.so_dien_thoai, kh.email, dh.ngay_dat, dh.ngay_giao, dh.trang_thai, dh.tong_tien, dh.da_coc " +
                "FROM don_hang dh JOIN khach_hang kh ON dh.ma_khach = kh.ma_khach";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("ma_don"));
                o.setCustomerId(rs.getInt("ma_khach"));
                o.setCustomerName(rs.getString("ho_ten"));
                o.setCustomerPhone(rs.getString("so_dien_thoai"));
                o.setCustomerEmail(rs.getString("email"));
                o.setOrderDate(rs.getDate("ngay_dat"));
                o.setDeliveryDate(rs.getDate("ngay_giao"));
                o.setStatus(rs.getString("trang_thai"));
                o.setTotal(rs.getDouble("tong_tien"));
                o.setDeposit(rs.getDouble("da_coc"));
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Order findById(int id) {
        String sql = "SELECT dh.ma_don, dh.ma_khach, kh.ho_ten, kh.so_dien_thoai, kh.email, dh.ngay_dat, dh.ngay_giao, dh.trang_thai, dh.tong_tien, dh.da_coc " +
                "FROM don_hang dh JOIN khach_hang kh ON dh.ma_khach = kh.ma_khach WHERE dh.ma_don=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setId(rs.getInt("ma_don"));
                    o.setCustomerId(rs.getInt("ma_khach"));
                    o.setCustomerName(rs.getString("ho_ten"));
                    o.setCustomerPhone(rs.getString("so_dien_thoai"));
                    o.setCustomerEmail(rs.getString("email"));
                    o.setOrderDate(rs.getDate("ngay_dat"));
                    o.setDeliveryDate(rs.getDate("ngay_giao"));
                    o.setStatus(rs.getString("trang_thai"));
                    o.setTotal(rs.getDouble("tong_tien"));
                    o.setDeposit(rs.getDouble("da_coc"));
                    return o;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(Order order) {
        String sql = "INSERT INTO don_hang(ma_khach, ngay_dat, ngay_giao, trang_thai, tong_tien, da_coc) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order.getCustomerId());
            ps.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
            ps.setDate(3, new java.sql.Date(order.getDeliveryDate().getTime()));
            ps.setString(4, order.getStatus());
            ps.setDouble(5, order.getTotal());
            ps.setDouble(6, order.getDeposit());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OrderDetail> findDetailsByCustomer(int customerId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT ct.ma_ct, ct.ma_don, ct.loai_sp, ct.ten_vai, ct.don_gia, ct.so_luong, ct.ghi_chu " +
                     "FROM chi_tiet_don ct JOIN don_hang dh ON ct.ma_don = dh.ma_don WHERE dh.ma_khach = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail d = new OrderDetail();
                    d.setId(rs.getInt("ma_ct"));
                    d.setOrderId(rs.getInt("ma_don"));
                    d.setProductType(rs.getString("loai_sp"));
                    d.setMaterialName(rs.getString("ten_vai"));
                    d.setUnitPrice(rs.getDouble("don_gia"));
                    d.setQuantity(rs.getInt("so_luong"));
                    d.setNote(rs.getString("ghi_chu"));
                    list.add(d);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
