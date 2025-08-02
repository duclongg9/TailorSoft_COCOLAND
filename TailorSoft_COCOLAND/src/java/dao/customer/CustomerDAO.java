package dao.customer;

import dao.connect.DBConnect;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT kh.ma_khach, kh.ho_ten, kh.so_dien_thoai, kh.email, kh.dia_chi, " +
                "COUNT(dh.ma_don) AS total_orders, " +
                "SUM(CASE WHEN dh.trang_thai IS NOT NULL AND dh.trang_thai <> 'Hoàn thành' THEN 1 ELSE 0 END) AS pending_orders " +
                "FROM khach_hang kh LEFT JOIN don_hang dh ON kh.ma_khach = dh.ma_khach " +
                "GROUP BY kh.ma_khach, kh.ho_ten, kh.so_dien_thoai, kh.email, kh.dia_chi";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("ma_khach"));
                c.setName(rs.getString("ho_ten"));
                c.setPhone(rs.getString("so_dien_thoai"));
                c.setEmail(rs.getString("email"));
                c.setAddress(rs.getString("dia_chi"));
                c.setTotalOrders(rs.getInt("total_orders"));
                c.setPendingOrders(rs.getInt("pending_orders"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Customer customer) {
        String sql = "INSERT INTO khach_hang(ho_ten, so_dien_thoai, email, dia_chi) VALUES(?,?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer findById(int id) {
        String sql = "SELECT kh.ma_khach, kh.ho_ten, kh.so_dien_thoai, kh.email, kh.dia_chi, " +
                "COUNT(dh.ma_don) AS total_orders, " +
                "SUM(CASE WHEN dh.trang_thai IS NOT NULL AND dh.trang_thai <> 'Hoàn thành' THEN 1 ELSE 0 END) AS pending_orders " +
                "FROM khach_hang kh LEFT JOIN don_hang dh ON kh.ma_khach = dh.ma_khach WHERE kh.ma_khach=? " +
                "GROUP BY kh.ma_khach, kh.ho_ten, kh.so_dien_thoai, kh.email, kh.dia_chi";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer c = new Customer();
                    c.setId(rs.getInt("ma_khach"));
                    c.setName(rs.getString("ho_ten"));
                    c.setPhone(rs.getString("so_dien_thoai"));
                    c.setEmail(rs.getString("email"));
                    c.setAddress(rs.getString("dia_chi"));
                    c.setTotalOrders(rs.getInt("total_orders"));
                    c.setPendingOrders(rs.getInt("pending_orders"));
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Customer customer) {
        String sql = "UPDATE khach_hang SET ho_ten=?, so_dien_thoai=?, email=?, dia_chi=? WHERE ma_khach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getAddress());
            ps.setInt(5, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
