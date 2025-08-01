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
}
