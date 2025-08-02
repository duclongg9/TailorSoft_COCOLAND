package dao.measurementtype;

import dao.connect.DBConnect;
import model.MeasurementType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeasurementTypeDAO {
    public List<MeasurementType> search(String bodyPart, String keyword) {
        List<MeasurementType> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT ma_thong_so, ten_thong_so, don_vi, bo_phan, ghi_chu FROM loai_thong_so WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (bodyPart != null && !bodyPart.isBlank()) {
            sql.append(" AND bo_phan = ?");
            params.add(bodyPart);
        }
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND ten_thong_so LIKE ?");
            params.add("%" + keyword + "%");
        }
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MeasurementType mt = new MeasurementType();
                    mt.setId(rs.getInt("ma_thong_so"));
                    mt.setName(rs.getString("ten_thong_so"));
                    mt.setUnit(rs.getString("don_vi"));
                    mt.setBodyPart(rs.getString("bo_phan"));
                    mt.setNote(rs.getString("ghi_chu"));
                    list.add(mt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> findBodyParts() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT bo_phan FROM loai_thong_so";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("bo_phan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(MeasurementType mt) {
        String sql = "INSERT INTO loai_thong_so(ten_thong_so, don_vi, bo_phan, ghi_chu) VALUES(?,?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mt.getName());
            ps.setString(2, mt.getUnit());
            ps.setString(3, mt.getBodyPart());
            ps.setString(4, mt.getNote());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MeasurementType findById(int id) {
        String sql = "SELECT ma_thong_so, ten_thong_so, don_vi, bo_phan, ghi_chu FROM loai_thong_so WHERE ma_thong_so = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MeasurementType mt = new MeasurementType();
                    mt.setId(rs.getInt("ma_thong_so"));
                    mt.setName(rs.getString("ten_thong_so"));
                    mt.setUnit(rs.getString("don_vi"));
                    mt.setBodyPart(rs.getString("bo_phan"));
                    mt.setNote(rs.getString("ghi_chu"));
                    return mt;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(MeasurementType mt) {
        String sql = "UPDATE loai_thong_so SET ten_thong_so = ?, don_vi = ?, bo_phan = ?, ghi_chu = ? WHERE ma_thong_so = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mt.getName());
            ps.setString(2, mt.getUnit());
            ps.setString(3, mt.getBodyPart());
            ps.setString(4, mt.getNote());
            ps.setInt(5, mt.getId());
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
