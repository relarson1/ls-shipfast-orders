package com.shipfast;

import com.shipfast.model.Order;
import com.shipfast.model.OrderRepository;
import io.advantageous.qbit.annotation.PathVariable;
import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequestMapping("/orders")
public class OrdersService {
	private static Logger log = LoggerFactory.getLogger(OrdersService.class);

	@Autowired
	private Services services;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RestTemplate restTemplate;

	@RequestMapping(value = "/",method = RequestMethod.POST)
	public Order placeOrder(Order order) {
		int stockQuantity = 0;

		try {
			Map<String, Object> itemDetails = restTemplate.getForObject(
				services.getURI("inventory") + "/items/" + order.getItemId(),
				Map.class
			);

			if (itemDetails != null) {
				stockQuantity = Integer.parseInt(String.valueOf(itemDetails.get("stockQuantity")));
			}
		}
		catch(RuntimeException ex) {
			log.error("Failed to get item details", ex);
			order.setStatus("failed");
		}

		if(stockQuantity > 0) {
			order.setStatus("pending");
			orderRepository.save(order);
		}
		else {
			order.setStatus("unavailable");
		}
		return order;
	}

	@RequestMapping("/{id}")
	public Order getOrder(@PathVariable("id") String id) {
		return orderRepository.findOne(id);
	}
}
