package com.shipfast;

import com.shipfast.model.Order;
import com.shipfast.model.OrderRepository;
import io.advantageous.qbit.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@RequestMapping("orders")
public class OrdersService {
	@Autowired
	private OrderRepository orderRepository;

	@RequestMapping("placeOrder")
	public String placeOrder(Order order) {
		return "Hello World";
	}
}
