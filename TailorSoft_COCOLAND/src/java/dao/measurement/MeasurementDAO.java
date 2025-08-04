package dao.measurement;

import dao.connect.DBConnect;
import model.Measurement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MeasurementDAO {
    private final Connection conn;

    public MeasurementDAO() {
        this.conn = null;
    }

    public MeasurementDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Measurement> findAll() {
        List<Measurement> list = new ArrayList<>();
        String sql = "SELECT tsd.ma_do, tsd.ma_khach, k.ho_ten, tsd.ma_loai, l.ten_loai, " +
                "tsd.ma_thong_so, t.ten_thong_so, tsd.gia_tri, tsd.ghi_chu " +
                "FROM thong_so_do tsd " +
                "JOIN khach_hang k ON tsd.ma_khach = k.ma_khach " +
                "JOIN loai_san_pham l ON tsd.ma_loai = l.ma_loai " +
                "JOIN loai_thong_so t ON tsd.ma_thong_so = t.ma_thong_so";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Measurement m = new Measurement();
                m.setId(rs.getInt("ma_do"));
                m.setCustomerId(rs.getInt("ma_khach"));
                m.setCustomerName(rs.getString("ho_ten"));
                m.setProductTypeId(rs.getInt("ma_loai"));
                m.setProductTypeName(rs.getString("ten_loai"));
                m.setMeasurementTypeId(rs.getInt("ma_thong_so"));
                m.setMeasurementTypeName(rs.getString("ten_thong_so"));
                m.setValue(rs.getDouble("gia_tri"));
                m.setNote(rs.getString("ghi_chu"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Measurement measurement) throws SQLException {
        String sql = "INSERT INTO thong_so_do(ma_khach, ma_loai, ma_thong_so, gia_tri, ghi_chu) VALUES(?,?,?,?,?)";
        if (conn != null) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, measurement.getCustomerId());
                ps.setInt(2, measurement.getProductTypeId());
                ps.setInt(3, measurement.getMeasurementTypeId());
                ps.setDouble(4, measurement.getValue());
                ps.setString(5, measurement.getNote());
                ps.executeUpdate();
            }
        } else {
            try (Connection c = DBConnect.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, measurement.getCustomerId());
                ps.setInt(2, measurement.getProductTypeId());
                ps.setInt(3, measurement.getMeasurementTypeId());
                ps.setDouble(4, measurement.getValue());
                ps.setString(5, measurement.getNote());
                ps.executeUpdate();
            }
        }
    }

    public List<Map<String, Object>> findByOrderDetail(int detailId) {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT tsd.ma_do, lt.ten_thong_so, lt.don_vi, tsd.gia_tri " +
                "FROM thong_so_do tsd JOIN loai_thong_so lt ON tsd.ma_thong_so = lt.ma_thong_so " +
                "WHERE tsd.ma_ct = ?";
        try (Connection c = DBConnect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, detailId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", rs.getInt("ma_do"));
                    m.put("name", rs.getString("ten_thong_so"));
                    m.put("unit", rs.getString("don_vi"));
                    m.put("value", rs.getDouble("gia_tri"));
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateValueById(Connection c, int id, double value) throws SQLException {
        String sql = "UPDATE thong_so_do SET gia_tri=? WHERE ma_do=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, value);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
