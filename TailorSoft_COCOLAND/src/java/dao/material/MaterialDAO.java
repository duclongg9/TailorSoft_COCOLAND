package dao.material;

import dao.connect.DBConnect;
import model.Material;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    public List<Material> findAll() {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT ma_vai, ten_vai, mau_sac, xuat_xu, gia_thanh, so_luong FROM kho_vai";
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
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Material material) {
        String sql = "INSERT INTO kho_vai(ten_vai, mau_sac, xuat_xu, gia_thanh, so_luong) VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, material.getName());
            ps.setString(2, material.getColor());
            ps.setString(3, material.getOrigin());
            ps.setDouble(4, material.getPrice());
            ps.setDouble(5, material.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
