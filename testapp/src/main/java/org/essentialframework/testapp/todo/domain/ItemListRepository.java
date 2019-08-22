package org.essentialframework.testapp.todo.domain;

import java.util.List;

public interface ItemListRepository {
	
	List<ItemList> findAll();
	
	ItemList findById(String id);
	
	void save(ItemList itemList);
	
	void delete(String id);

}
