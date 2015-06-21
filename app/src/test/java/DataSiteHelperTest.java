import com.example.rush0714.myapplication.DataStorage;

import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import NetUtils.Orders.Comment;
import NetUtils.Orders.Order;
import NetUtils.Orders.OrderHelper;

import static org.junit.Assert.assertEquals;

public class DataSiteHelperTest {
    @Test
    public void a() throws IOException, ParseException {
        final Order order = new Order(null, null, null, null, null, null);
        order.setComments(new ArrayList<Comment>() {{
                              add(new Comment(null, null, null, "text text text 12312312332; 555666", null));
                              add(new Comment(null, null, null, "text text text 111222; 777777777777777777777777777", null));
                              add(new Comment(null, null, null, "text text text 888888 ", null));
                              add(new Comment(null, null, null, "text text text +7 (111) 111-1111", null));
                              add(new Comment(null, null, null, "text text +7 (222) 111-1111 ", null));
                              add(new Comment(null, null, null, "+7 (333) 111-1111", null));
                          }}
        );
        DataStorage.setOrders(new ArrayList<Order>() {{
                                  add(order);
                              }}
        );
        assertEquals(
            Arrays.asList("555666", "+72312312332", "111222", "+72221111111", "888888", "+71111111111","+73331111111"),
            OrderHelper.parsePhoneNumbers(0)
        );
    }
}

