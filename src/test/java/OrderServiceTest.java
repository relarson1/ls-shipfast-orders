import com.shipfast.OrdersService;
import com.shipfast.Services;
import com.shipfast.model.Order;
import com.shipfast.model.OrderRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
		// Arrange
		Map<String, Object> item1 = new HashMap<>();
		item1.put("id", "1234");
		item1.put("stockQuantity", 5);

		Map<String, Object> item2 = new HashMap<>();
		item2.put("id", "4567");
		item2.put("stockQuantity", 100);

		when(services.getURI("inventory")).thenReturn("http://inventory/api");

		when(restTemplate.getForObject(
			"http://inventory/api/items/1234",
			Map.class
		)).thenReturn(item1);

		when(restTemplate.getForObject(
			"http://inventory/api/items/4567",
			Map.class
		)).thenReturn(item2);

		// Create an order for an item whose in-stock quantity is not
		// enough to cover the order
		Order order1 = new Order();
		order1.setItemId("1234");
		order1.setQuantity(10);

		// Create an order for an item whose stock is in sufficient quantity
		Order order2 = new Order();
		order2.setItemId("4567");
		order2.setQuantity(50);

		// Act
		Order response1 = ordersService.placeOrder(order1);
		Order response2 = ordersService.placeOrder(order2);

		// Expect
		Assert.assertNotNull(response1);
		Assert.assertEquals(response1.getQuantity(), order1.getQuantity());
		Assert.assertEquals(response1.getStatus(), "unavailable");

		Assert.assertNotNull(response2);
		Assert.assertEquals(response2.getQuantity(), order2.getQuantity());
		Assert.assertEquals(response2.getStatus(), "pending");
	}
}
