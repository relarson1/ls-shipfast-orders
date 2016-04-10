package com.shipfast;

import io.advantageous.qbit.http.HttpTransport;
import io.advantageous.qbit.server.ServiceEndpointServer;
import io.advantageous.qbit.servlet.QBitHttpServlet;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletConfig;

import static io.advantageous.qbit.server.EndpointServerBuilder.endpointServerBuilder;

public class DispatcherServlet extends QBitHttpServlet {
	@Autowired
	private InventoryService inventoryService;

	private ServiceEndpointServer serviceServer;

	@Override
	protected void stop() {
		serviceServer.stop();
	}

	@Override
	protected void wireHttpServer(final HttpTransport httpTransport,
								  final ServletConfig servletConfig) {
		serviceServer = endpointServerBuilder()
			.setUri("/api")
			.setHttpTransport(httpTransport).build();
		serviceServer.initServices(
			inventoryService
		).startServer();
	}
}
