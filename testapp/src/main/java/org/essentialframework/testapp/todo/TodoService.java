package org.essentialframework.testapp.todo;

import java.util.List;

import org.essentialframework.testapp.todo.domain.Item;
import org.essentialframework.testapp.todo.domain.ItemList;

public interface TodoService {
	
	List<ItemList> getItemLists();
	
	List<Item> getItems(String itemListId);
	
	List<Item> getAllStarredItems();

	List<Item> getStarredItemsFromList(String itemListId);
	
	List<Item> getAllCompletedItems();
	
	List<Item> getCompletedItemsFromList(String itemListId);
	
	void save(ItemList itemList) throws TodoServiceException;
	
	void save(Item item) throws TodoServiceException;
	
	void markAsCompleted(String itemId);
	
	void markAsStarred(String itemId);
	
	List<ItemList> searchItemListsByName(String name);
	
	List<Item> searchItemsFromListByName(String itemListId, String name);
	
	void deleteItemList(String itemListId);
	
	void deleteItem(String itemId);

}
