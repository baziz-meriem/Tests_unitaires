import com.example.dao.OrderDao;
import com.example.dao.StockDao;
import com.example.entity.Customer;
import com.example.entity.Order;
import com.example.entity.Orderline;
import com.example.entity.Product;
import com.example.service.IPayementManager;
import com.example.service.IStockManager;
import com.example.service.OrderManager;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;

public class OrderManagerTest extends TestCase {
    OrderManager orderManager = new OrderManager();

    public void testCreateOrderfalse() {

        Product product = mock(Product.class);

        Orderline orderline = new Orderline();
        orderline.setProduct(product);
        orderline.setOrderedQte(200);//more than qt in stock

        List<Orderline> orders = new ArrayList<Orderline>();
        orders.add(orderline);

        Order order = mock(Order.class);
        when(order.getOrderlines()).thenReturn(orders);
        //mock creation
        IStockManager stockManager = mock(IStockManager.class);
        //value to be returned when method is called
        when(stockManager.getProductQte(product)).thenReturn(20);
        orderManager.setiStockManager(stockManager);

        //product quantity is more than the actual stock
        assertEquals(false, orderManager.createOrder(order));

    }
    @Test
    public void testCreateOrdertrue() {
        OrderDao orderDao = mock(OrderDao.class);
         orderManager.setOrderDao(orderDao);

        Product product1 = mock(Product.class);

        Orderline orderline = new Orderline();
        orderline.setProduct(product1);
        orderline.setOrderedQte(200);//less than qt in stock

        List<Orderline> orders = new ArrayList<Orderline>();
        orders.add(orderline);

        Order order = mock(Order.class);
        when(order.getOrderlines()).thenReturn(orders);
        //mock creation
        IStockManager stockManager = mock(IStockManager.class);
        //value to be returned when method is called
        when(stockManager.getProductQte(product1)).thenReturn(800);
        orderManager.setiStockManager(stockManager);

        //product quantity is less than the actual stock
        assertEquals(true, orderManager.createOrder(order));
    }

@Test
    public void testCancelOrderFalse() {
        IPayementManager ipayementManager= mock(IPayementManager.class);
        when(ipayementManager.isPaid(1)).thenReturn(true);
        OrderDao orderDao = mock(OrderDao.class);
        orderManager.setiPayementManager(ipayementManager);

        //the order has already been paid no exchange is allowed
        assertEquals(false, orderManager.cancelOrder(1));

    }
@Test
    public void testCancelOrdertrue() {
        IPayementManager ipayementManager= mock(IPayementManager.class);
        when(ipayementManager.isPaid(1)).thenReturn(false);
        orderManager.setiPayementManager(ipayementManager);
        Product product = mock(Product.class);

        Orderline orderline = new Orderline();
        orderline.setProduct(product);
        orderline.setOrderedQte(200);//more than qt in stock

        List<Orderline> orders = new ArrayList<Orderline>();
        orders.add(orderline);
        OrderDao orderDao = mock(OrderDao.class);
        orderManager.setOrderDao(orderDao);

        Order order = mock(Order.class);
        when(order.getOrderlines()).thenReturn(orders);


        IStockManager StockManager = mock(IStockManager .class);
        when(StockManager.getProductQte(product)).thenReturn(800);
        orderManager.setiStockManager(StockManager);
        //the order has already been paid no exchange is allowed
        assertEquals(true, orderManager.cancelOrder(1));
    }
}