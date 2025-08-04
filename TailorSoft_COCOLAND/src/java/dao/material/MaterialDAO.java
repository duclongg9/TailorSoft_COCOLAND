package dao.material;

import dao.connect.DBConnect;
import model.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    private final Connection conn;

    public MaterialDAO() {
        this.conn = null;
    }

    public MaterialDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Material> findAll() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT ma_vai, ten_vai, mau_sac, xuat_xu, gia_thanh, so_luong, hinh_hoa_don FROM kho_vai";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Material m = new Material();
                m.setId(rs.getInt("ma_vai"));
                m.setName(rs.getString("ten_vai"));
                m.setColor(rs.getString("mau_sac"));
                m.setOrigin(rs.getString("xuat_xu"));
                m.setPrice(rs.getDouble("gia_thanh"));
                m.setQuantity(rs.getDouble("so_luong"));
                m.setInvoiceImage(rs.getString("hinh_hoa_don"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Material material) {
        String sql = "INSERT INTO kho_vai(ten_vai, mau_sac, xuat_xu, gia_thanh, so_luong, hinh_hoa_don) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, material.getName());
            ps.setString(2, material.getColor());
            ps.setString(3, material.getOrigin());
            ps.setDouble(4, material.getPrice());
            ps.setDouble(5, material.getQuantity());
            ps.setString(6, material.getInvoiceImage());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Material findById(int id) {
        String sql = "SELECT ma_vai, ten_vai, mau_sac, xuat_xu, gia_thanh, so_luong, hinh_hoa_don FROM kho_vai WHERE ma_vai=?";
        Connection c = conn;
        boolean shouldClose = false;
        if (c == null) {
            c = DBConnect.getConnection();
            shouldClose = true;
        }
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Material m = new Material();
                    m.setId(rs.getInt("ma_vai"));
                    m.setName(rs.getString("ten_vai"));
                    m.setColor(rs.getString("mau_sac"));
                    m.setOrigin(rs.getString("xuat_xu"));
                    m.setPrice(rs.getDouble("gia_thanh"));
                    m.setQuantity(rs.getDouble("so_luong"));
                    m.setInvoiceImage(rs.getString("hinh_hoa_don"));
                    return m;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            } finally {
            if (shouldClose && c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void decreaseQuantity(int id, double amount) throws SQLException {
        String sql = "UPDATE kho_vai SET so_luong = so_luong - ? WHERE ma_vai = ?";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, amount);
                ps.setInt(2, id);
                ps.executeUpdate();
            }
        } else {
            try (Connection c = DBConnect.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setDouble(1, amount);
                ps.setInt(2, id);
                ps.executeUpdate();
            }
        }
    }
}
