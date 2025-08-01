package dao.measurement;

import dao.connect.DBConnect;
import model.Measurement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeasurementDAO {
    public List<Measurement> findAll() {
        List<Measurement> list = new ArrayList<>();
        String sql = "SELECT ma_do, ma_khach, ma_loai, ma_thong_so, gia_tri, ghi_chu FROM thong_so_do";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Measurement m = new Measurement();
                m.setId(rs.getInt("ma_do"));
                m.setCustomerId(rs.getInt("ma_khach"));
                m.setProductTypeId(rs.getInt("ma_loai"));
                m.setMeasurementTypeId(rs.getInt("ma_thong_so"));
                m.setValue(rs.getDouble("gia_tri"));
                m.setNote(rs.getString("ghi_chu"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Measurement measurement) {
        String sql = "INSERT INTO thong_so_do(ma_khach, ma_loai, ma_thong_so, gia_tri, ghi_chu) VALUES(?,?,?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, measurement.getCustomerId());
            ps.setInt(2, measurement.getProductTypeId());
            ps.setInt(3, measurement.getMeasurementTypeId());
            ps.setDouble(4, measurement.getValue());
            ps.setString(5, measurement.getNote());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
