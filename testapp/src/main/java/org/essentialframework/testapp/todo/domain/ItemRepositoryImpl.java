package org.essentialframework.testapp.todo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.essentialframework.core.annotation.Repository;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

	private Map<String, Item> itemMap = new ConcurrentHashMap<>(0);
	
	@Override
	public Item findById(String id) {
		return itemMap.get(id);
	}
	
	@Override
	public List<Item> findAll() {
		return new ArrayList<>(itemMap.values());
	}
	
	@Override
	public List<Item> findByItemListId(String id) {
		return itemMap.values().stream()
			.filter(item -> item.getItemListId().equals(id))
			.collect(Collectors.toList());
	}

	@Override
	public void save(Item item) {
		if(item == null) {
			throw new IllegalArgumentException("Cannot save a null Item");
		}
		
		if(item.getId() == null) {
			item.setId(UUID.randomUUID().toString());
		}
		
		itemMap.put(item.getId(), item);
	}

	@Override
	public void delete(String id) {
		itemMap.remove(id);
	}

}
