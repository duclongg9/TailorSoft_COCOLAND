package dao.order;

import dao.connect.DBConnect;
import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT ma_don, ma_khach, ngay_dat, ngay_giao, trang_thai, tong_tien, da_coc FROM don_hang";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("ma_don"));
                o.setCustomerId(rs.getInt("ma_khach"));
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
        String sql = "SELECT ma_don, ma_khach, ngay_dat, ngay_giao, trang_thai, tong_tien, da_coc FROM don_hang WHERE ma_don=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setId(rs.getInt("ma_don"));
                    o.setCustomerId(rs.getInt("ma_khach"));
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
}
