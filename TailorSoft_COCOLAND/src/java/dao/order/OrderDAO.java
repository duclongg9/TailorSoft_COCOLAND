package dao.order;

import dao.connect.DBConnect;
import model.Order;
import model.OrderDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private final Connection conn;

    public OrderDAO() {
        this.conn = null;
    }

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }
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

    public int insert(Order order) throws SQLException {
        String sql = "INSERT INTO don_hang(ma_khach, ngay_dat, ngay_giao, trang_thai, tong_tien, da_coc) VALUES(?,?,?,?,?,?)";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getCustomerId());
                ps.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
                ps.setDate(3, new java.sql.Date(order.getDeliveryDate().getTime()));
                ps.setString(4, order.getStatus());
                ps.setDouble(5, order.getTotal());
                ps.setDouble(6, order.getDeposit());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        } else {
            try (Connection c = DBConnect.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getCustomerId());
                ps.setDate(2, new java.sql.Date(order.getOrderDate().getTime()));
                ps.setDate(3, new java.sql.Date(order.getDeliveryDate().getTime()));
                ps.setString(4, order.getStatus());
                ps.setDouble(5, order.getTotal());
                ps.setDouble(6, order.getDeposit());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        }
    }

    public int insertDetail(OrderDetail detail) throws SQLException {
        String sql = "INSERT INTO chi_tiet_don(ma_don, loai_sp, ten_vai, don_gia, so_luong, ghi_chu) VALUES(?,?,?,?,?,?)";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, detail.getOrderId());
                ps.setString(2, detail.getProductType());
                ps.setString(3, detail.getMaterialName());
                ps.setDouble(4, detail.getUnitPrice());
                ps.setInt(5, detail.getQuantity());
                ps.setString(6, detail.getNote());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        } else {
            try (Connection c = DBConnect.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, detail.getOrderId());
                ps.setString(2, detail.getProductType());
                ps.setString(3, detail.getMaterialName());
                ps.setDouble(4, detail.getUnitPrice());
                ps.setInt(5, detail.getQuantity());
                ps.setString(6, detail.getNote());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
            return -1;
        }
    }

    public List<OrderDetail> findDetailsByOrder(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT ma_ct, ma_don, loai_sp, ten_vai, don_gia, so_luong, ghi_chu FROM chi_tiet_don WHERE ma_don = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
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

    public List<Order> findByCustomer(int customerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT dh.ma_don, dh.ngay_dat, dh.ngay_giao, dh.trang_thai, dh.tong_tien, " +
                "GROUP_CONCAT(ct.loai_sp SEPARATOR ', ') AS san_pham " +
                "FROM don_hang dh LEFT JOIN chi_tiet_don ct ON dh.ma_don = ct.ma_don " +
                "WHERE dh.ma_khach = ? GROUP BY dh.ma_don, dh.ngay_dat, dh.ngay_giao, dh.trang_thai, dh.tong_tien " +
                "ORDER BY dh.ma_don DESC";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setId(rs.getInt("ma_don"));
                    o.setOrderDate(rs.getDate("ngay_dat"));
                    o.setDeliveryDate(rs.getDate("ngay_giao"));
                    o.setStatus(rs.getString("trang_thai"));
                    o.setTotal(rs.getDouble("tong_tien"));
                    o.setProductType(rs.getString("san_pham"));
                    list.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateDetail(Connection c, int detailId, int qty, String note, double unitPrice) throws SQLException {
        int orderId = 0;
        String getSql = "SELECT ma_don FROM chi_tiet_don WHERE ma_ct=?";
        try (PreparedStatement ps = c.prepareStatement(getSql)) {
            ps.setInt(1, detailId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    orderId = rs.getInt(1);
                }
            }
        }
        String updateSql = "UPDATE chi_tiet_don SET so_luong=?, ghi_chu=?, don_gia=? WHERE ma_ct=?";
        try (PreparedStatement ps = c.prepareStatement(updateSql)) {
            ps.setInt(1, qty);
            ps.setString(2, note);
            ps.setDouble(3, unitPrice);
            ps.setInt(4, detailId);
            ps.executeUpdate();
        }
        String totalSql = "UPDATE don_hang SET tong_tien = (SELECT SUM(so_luong*don_gia) FROM chi_tiet_don WHERE ma_don=?) WHERE ma_don=?";
        try (PreparedStatement ps = c.prepareStatement(totalSql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public int updateStatus(int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE don_hang SET trang_thai=? WHERE ma_don=?";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newStatus);
                ps.setInt(2, orderId);
                return ps.executeUpdate();
            }
        } else {
            try (Connection c = DBConnect.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, newStatus);
                ps.setInt(2, orderId);
                return ps.executeUpdate();
            }
        }
    }

    public void updateAmounts(int orderId, double total, double deposit) throws SQLException {
        String sql = "UPDATE don_hang SET tong_tien=?, da_coc=? WHERE ma_don=?";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, total);
                ps.setDouble(2, deposit);
                ps.setInt(3, orderId);
                ps.executeUpdate();
            }
        } else {
            try (Connection c = DBConnect.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setDouble(1, total);
                ps.setDouble(2, deposit);
                ps.setInt(3, orderId);
                ps.executeUpdate();
            }
        }
    }
}
