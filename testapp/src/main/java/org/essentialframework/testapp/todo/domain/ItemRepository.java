package org.essentialframework.testapp.todo.domain;
import java.util.List;

public interface ItemRepository {
	
	List<Item> findAll();
	
	Item findById(String id);
	
	List<Item> findByItemListId(String id);
	
	void save(Item item);
	
	void delete(String id);

}
