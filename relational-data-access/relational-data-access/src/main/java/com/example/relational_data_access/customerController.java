package com.example.relational_data_access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RestController
public class customerController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/customers")
    public List<Customer> getCustomersByName(@RequestParam String name) {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name FROM customers WHERE first_name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    customers.add(new Customer(
                            resultSet.getLong("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }
}
