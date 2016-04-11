import com.shipfast.Application;
import com.shipfast.OrdersService;
import com.shipfast.Services;
import com.shipfast.model.Order;
import com.shipfast.model.OrderRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class OrderServiceTest {
	@InjectMocks
	private OrdersService ordersService;

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private Services services;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void ensureItemInStockWhenOrdering() {
		Map<String, Object> inventoryCheck = new HashMap<>();
		inventoryCheck.put("id", "1234");
		inventoryCheck.put("stockQuantity", 5);

		when(services.getURI("inventory")).thenReturn("http://inventory/api");
		when(restTemplate.getForObject(
			"http://inventory/api/items/1234",
			Map.class
		)).thenReturn(inventoryCheck);

		Order order = new Order();
		order.setItemId("1234");
		order.setQuantity(5);

		Order response = ordersService.placeOrder(order);

		Assert.assertNotNull(response);
		Assert.assertEquals(response.getQuantity(), order.getQuantity());
		Assert.assertEquals(response.getStatus(), "pending");
	}
}
