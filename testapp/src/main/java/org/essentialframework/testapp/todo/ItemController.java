package org.essentialframework.testapp.todo;

import java.util.List;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.core.annotation.Wired;
import org.essentialframework.testapp.todo.domain.Item;
import org.essentialframework.testapp.todo.misc.ItemDto;
import org.essentialframework.testapp.todo.misc.MappingUtil;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.PathVariable;
import org.essentialframework.web.annotation.RequestBody;
import org.essentialframework.web.annotation.RequestHandler;

@Controller
public class ItemController {

	@Wired
	private TodoService todoService;
	
	@RequestHandler(method = HttpMethod.GET, url = "/items/starred")
	public List<Item> getStarredItems() {
		return todoService.getAllStarredItems();
	}
	
	@RequestHandler(method = HttpMethod.GET, url = "/items/completed")
	public List<Item> getCompleted() {
		return todoService.getAllCompletedItems();
	}
	
	@RequestHandler(method = HttpMethod.GET, url = "/list/{id}/items")
	public List<Item> getItemsFromList( @PathVariable("id") String itemListId ) {
		return todoService.getItems(itemListId);
	}
	
	@RequestHandler(method = HttpMethod.GET, url = "/list/{id}/items/starred")
	public List<Item> getStarredItemsFromList( @PathVariable("id") String itemListId ) {
		return todoService.getStarredItemsFromList(itemListId);
	}
	
	@RequestHandler(method = HttpMethod.GET, url = "/list/{id}/items/completed")
	public List<Item> getCompletedItemsFromList( @PathVariable("id") String itemListId ) {
		return todoService.getStarredItemsFromList(itemListId);
	}
	
	@RequestHandler(method = HttpMethod.POST, url="/list/{id}/items/add")
	public Item createNewItem( @PathVariable("id") String itemListId, @RequestBody ItemDto itemDto ) 
			throws TodoServiceException {
		final Item item = MappingUtil.map(itemDto);
		todoService.save(item);
		return item;
	}
	
	@RequestHandler(method = HttpMethod.PUT, url = "/item/{id}/starred")
	public void markAsStarred( @PathVariable("id") String itemId) {
		todoService.markAsStarred(itemId);
	}
	
	@RequestHandler(method = HttpMethod.PUT, url = "/item/{id}/completed")
	public void markAsCompleted( @PathVariable("id") String itemId) {
		todoService.markAsCompleted(itemId);
	}
	
	@RequestHandler(method = HttpMethod.DELETE, url="/item/{id}")
	public void deleteItemList( @PathVariable("id") String itemId ) {
		todoService.deleteItem(itemId);
	}
	
}
