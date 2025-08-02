package dao.measurementtype;

import dao.connect.DBConnect;
import model.MeasurementType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeasurementTypeDAO {
    public List<MeasurementType> findAll() {
        List<MeasurementType> list = new ArrayList<>();
        String sql = "SELECT ma_thong_so, ten_thong_so, don_vi FROM loai_thong_so";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MeasurementType mt = new MeasurementType();
                mt.setId(rs.getInt("ma_thong_so"));
                mt.setName(rs.getString("ten_thong_so"));
                mt.setUnit(rs.getString("don_vi"));
                list.add(mt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(MeasurementType mt) {
        String sql = "INSERT INTO loai_thong_so(ten_thong_so, don_vi) VALUES(?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mt.getName());
            ps.setString(2, mt.getUnit());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public MeasurementType findById(int id) {
        String sql = "SELECT ma_thong_so, ten_thong_so, don_vi FROM loai_thong_so WHERE ma_thong_so = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MeasurementType mt = new MeasurementType();
                    mt.setId(rs.getInt("ma_thong_so"));
                    mt.setName(rs.getString("ten_thong_so"));
                    mt.setUnit(rs.getString("don_vi"));
                    return mt;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(MeasurementType mt) {
        String sql = "UPDATE loai_thong_so SET ten_thong_so = ?, don_vi = ? WHERE ma_thong_so = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mt.getName());
            ps.setString(2, mt.getUnit());
            ps.setInt(3, mt.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM loai_thong_so WHERE ma_thong_so = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
