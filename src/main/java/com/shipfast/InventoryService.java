package com.shipfast;

import com.shipfast.model.Item;
import com.shipfast.model.ItemRepository;
import io.advantageous.qbit.annotation.PathVariable;
import io.advantageous.qbit.annotation.RequestMapping;
import io.advantageous.qbit.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequestMapping("/inventory")
public class InventoryService {
	private static Logger logger = LoggerFactory.getLogger(InventoryService.class);

	@Autowired
	private ItemRepository itemRepository;

	@RequestMapping("/items/{id}")
	public Item getItem(@PathVariable("id") String id) {
		Item item = itemRepository.findOne(id);
		logger.info("Finding item with id:" + id + "; " + item);
		return item;
	}

	@RequestMapping(value = "/items", method = RequestMethod.POST)
	public Item addItem(Item item) {
		return itemRepository.save(item);
	}

	@RequestMapping("/items")
	public List<Item> getItems() {
		return itemRepository.findAll();
	}

	@RequestMapping(value = "/items/{id}", method = RequestMethod.DELETE)
	public boolean removeItem(@PathVariable("id") String id)  {
		itemRepository.delete(id);
		return true;
	}
}
