package tests;

import com.example.dao.DatabaseConnection;
import com.example.dao.OrderDao;
import com.example.entity.Customer;
import com.example.entity.Order;
import com.example.entity.Orderline;
import com.example.entity.Product;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class OrderDaoTest {
    static DatabaseConnection  dbConnection ;
    static Connection connection;
    OrderDao orderDao ;


    @BeforeClass
    public static void init () {
        dbConnection = new DatabaseConnection("sa", "","org.h2.Driver","jdbc:h2:mem:test");
        connection = dbConnection.connect();
        dbConnection.createDb(connection);

    }
    @AfterClass
    public  static void clear (){
        dbConnection.disconnect(connection);

    }
    @Test
    public void insertTest() {
        Customer customer = new Customer();
        customer.setCustomerID("c1");

        Product product = new Product();
        product.setProductID("p1");

        Orderline orderline = new Orderline();
        orderline.setProduct(product);
        orderline.setOrderedQte(2);

        List<Orderline> orders = new ArrayList<Orderline>();
        orders.add(orderline);

        Order order = new Order();
        order.setOrderNum(1);
        order.setCustomer(customer);
        order.setOrderDate("12-12-2022");
        order.setOrderlines(orders);

         orderDao = new OrderDao();
        orderDao.setConn(connection);
        orderDao.insertOrder(order);
       List<Orderline> result =  orderDao.getOrderDetails(1);
       assertEquals(orderline, result.get(0));


    }
    @Test
    public void deleteTest () {
        Customer customer = new Customer();
        customer.setCustomerID("c1");
        Product product = new Product();
        product.setProductID("p1");
        List<Orderline> orders = new ArrayList<Orderline>();
        Orderline orderline = new Orderline();
        orderline.setProduct(product);
        orderline.setOrderedQte(2);
        orders.add(orderline);
        Order order = new Order();
        order.setOrderNum(2);
        order.setCustomer(customer);
        order.setOrderDate("12-12-2022");
        order.setOrderlines(orders);
        orderDao = new OrderDao();
        orderDao.setConn(connection);
        orderDao.insertOrder(order);
        List<Orderline> result =  orderDao.getOrderDetails(2);
        assertEquals(orderline, result.get(0));
        orderDao.deleteOrder(2);
        List<Orderline> result2 = orderDao.getOrderDetails(2);
        assertNull(result2);
    }


}