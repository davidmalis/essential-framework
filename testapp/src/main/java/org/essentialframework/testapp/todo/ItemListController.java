package org.essentialframework.testapp.todo;

import java.util.List;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.core.annotation.Wired;
import org.essentialframework.testapp.todo.domain.ItemList;
import org.essentialframework.testapp.todo.misc.ItemListDto;
import org.essentialframework.testapp.todo.misc.MappingUtil;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.PathVariable;
import org.essentialframework.web.annotation.RequestBody;
import org.essentialframework.web.annotation.RequestHandler;

@Controller
public class ItemListController {
	
	@Wired
	private TodoService todoService;
	
	@RequestHandler(method = HttpMethod.GET, url = "/lists")
	public List<ItemList> getItemLists(){
		return todoService.getItemLists();
	}
	
	@RequestHandler(method = HttpMethod.POST, url = "/lists/add")
	public ItemList createNewItemList( @RequestBody ItemListDto itemListDto ) 
			throws TodoServiceException {
		final ItemList itemList = MappingUtil.map(itemListDto);
		todoService.save(itemList);
		return itemList;
	}
	
	@RequestHandler(method = HttpMethod.DELETE, url="/list/{id}")
	public void deleteItemList( @PathVariable("id") String itemListId ) {
		todoService.deleteItemList(itemListId);
	}

}
