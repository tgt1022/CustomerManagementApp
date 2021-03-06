package com.springapp.mvc.dao;

import com.springapp.mvc.model.Customer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by heval-Computer on 23.2.2015.
 */

public class CustomerDAOImpl implements ICustomerDAO {

    private JdbcTemplate jdbcTemplate;

    public CustomerDAOImpl(DataSource dataSource){
        jdbcTemplate=new JdbcTemplate(dataSource);
    }

    @Override
    public void insertOrUpdateCustomer(Customer customer) {
        if(customer.getId()>0){
            String sqlQuery="UPDATE CUSTOMER SET NAME=?,"+"SURNAME=?,AGE=?,PASSWORD=? WHERE ID=?";
            jdbcTemplate.update(sqlQuery,customer.getName(),customer.getSurname(),customer.getAge(),customer.getId(),customer.getPassword());
        }else{
            String sqlQuery="INSERT INTO CUSTOMER(NAME,SURNAME,AGE,PASSWORD)"+"VALUES(?,?,?,?)";
            jdbcTemplate.update(sqlQuery,customer.getName(),customer.getSurname(),customer.getAge(),customer.getPassword());
        }
    }

    @Override
    public void delete(int customerId) {
        String sqlQuery="DELETE FROM CUSTOMER WHERE ID=?";
        jdbcTemplate.update(sqlQuery,customerId);
    }

    @Override
    public Customer getCustomer(final int customerId) {
        String sqlQuery="SELECT * FROM CUSTOMER WHERE ID="+customerId;
        return jdbcTemplate.query(sqlQuery, new ResultSetExtractor<Customer>() {
            @Override
            public Customer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
                if(resultSet.next()) {
                    Customer customer=new Customer();
                    customer.setId(resultSet.getInt("id"));
                    customer.setName(resultSet.getString("name"));
                    customer.setSurname(resultSet.getString("surname"));
                    customer.setAge(resultSet.getInt("age"));
                    return customer;
                }
                return null;
            }
        });
    }

    @Override
    public List<Customer> getCustomerList() {
        String sqlQuery="SELECT * FROM CUSTOMER";
        List<Customer> customerList=jdbcTemplate.query(sqlQuery, new RowMapper<Customer>() {

            @Override
            public Customer mapRow(ResultSet resultSet,int rowNumber) throws SQLException {
                Customer customer=new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                customer.setSurname(resultSet.getString("surname"));
                customer.setAge(resultSet.getInt("age"));
                return customer;
            }
        });
        return customerList;
    }
}
