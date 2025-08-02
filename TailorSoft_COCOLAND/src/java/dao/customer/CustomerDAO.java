package dao.customer;

import dao.connect.DBConnect;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        // Table khach_hang stores customer information
        String sql = "SELECT ma_khach, ho_ten, so_dien_thoai, email, ngay_tao FROM khach_hang";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt("ma_khach"));
                c.setName(rs.getString("ho_ten"));
                c.setPhone(rs.getString("so_dien_thoai"));
                c.setEmail(rs.getString("email"));
                c.setCreatedAt(rs.getTimestamp("ngay_tao"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Customer customer) {
        String sql = "INSERT INTO khach_hang(ho_ten, so_dien_thoai, email) VALUES(?,?,?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Customer findById(int id) {
        String sql = "SELECT ma_khach, ho_ten, so_dien_thoai, email, ngay_tao FROM khach_hang WHERE ma_khach=?";
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
                    c.setCreatedAt(rs.getTimestamp("ngay_tao"));
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Customer customer) {
        String sql = "UPDATE khach_hang SET ho_ten=?, so_dien_thoai=?, email=? WHERE ma_khach=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setInt(4, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
