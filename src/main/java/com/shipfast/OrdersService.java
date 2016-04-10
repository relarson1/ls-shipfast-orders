package com.shipfast;

import io.advantageous.qbit.annotation.RequestMapping;

@RequestMapping("orders")
public class OrdersService {
	@RequestMapping("placeOrder")
	public String placeOrder() {
		return "Hello World";
	}
}
