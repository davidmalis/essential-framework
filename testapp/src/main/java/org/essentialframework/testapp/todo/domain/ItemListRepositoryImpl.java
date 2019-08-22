package org.essentialframework.testapp.todo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.essentialframework.core.annotation.Repository;

@Repository
public class ItemListRepositoryImpl implements ItemListRepository {

	private Map<String, ItemList> itemListMap = new ConcurrentHashMap<>(0);
	
	@Override
	public List<ItemList> findAll() {
		return new ArrayList<>(itemListMap.values());
	}

	@Override
	public ItemList findById(String uuid) {
		return itemListMap.get(uuid);
	}

	@Override
	public void save(ItemList itemList) {
		if(itemList == null) {
			throw new IllegalArgumentException("Cannot save a null ItemList");
		}
		
		if(itemList.getId() == null) {
			itemList.setId(UUID.randomUUID().toString());
		}
		
		itemListMap.put(itemList.getId(), itemList);
		
	}

	@Override
	public void delete(String id) {
		itemListMap.remove(id);
	}

}
