package dao.producttype;

import dao.connect.DBConnect;
import model.ProductType;
import model.MeasurementType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductTypeDAO {
    public List<ProductType> findAll() {
        List<ProductType> list = new ArrayList<>();
        String sql = "SELECT ma_loai, ten_loai, ky_hieu FROM loai_san_pham";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new ProductType(rs.getInt("ma_loai"), rs.getString("ten_loai"), rs.getString("ky_hieu")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(ProductType pt, List<Integer> measurementTypeIds) {
        String sql = "INSERT INTO loai_san_pham(ten_loai, ky_hieu) VALUES(?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pt.getName());
            ps.setString(2, pt.getCode());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pt.setId(rs.getInt(1));
                }
            }
            if (measurementTypeIds != null) {
                sql = "INSERT INTO loai_sp_thong_so(ma_loai, ma_thong_so) VALUES(?,?)";
                try (PreparedStatement ps2 = conn.prepareStatement(sql)) {
                    for (int mtId : measurementTypeIds) {
                        ps2.setInt(1, pt.getId());
                        ps2.setInt(2, mtId);
                        ps2.addBatch();
                    }
                    ps2.executeBatch();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<MeasurementType> findMeasurementTypes(int productTypeId) {
        List<MeasurementType> list = new ArrayList<>();
        String sql = "SELECT t.ma_thong_so, t.ten_thong_so, t.don_vi " +
                "FROM loai_thong_so t JOIN loai_sp_thong_so l ON t.ma_thong_so = l.ma_thong_so " +
                "WHERE l.ma_loai = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productTypeId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MeasurementType mt = new MeasurementType();
                    mt.setId(rs.getInt("ma_thong_so"));
                    mt.setName(rs.getString("ten_thong_so"));
                    mt.setUnit(rs.getString("don_vi"));
                    list.add(mt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
