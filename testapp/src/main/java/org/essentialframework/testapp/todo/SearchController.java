package org.essentialframework.testapp.todo;

import java.util.List;
import java.util.Optional;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.core.annotation.Wired;
import org.essentialframework.testapp.todo.domain.Item;
import org.essentialframework.testapp.todo.domain.ItemList;
import org.essentialframework.web.HttpMethod;
import org.essentialframework.web.annotation.PathVariable;
import org.essentialframework.web.annotation.RequestHandler;
import org.essentialframework.web.annotation.RequestParameter;

@Controller
public class SearchController {

	@Wired
	private TodoService todoService;
	
	@RequestHandler(method = HttpMethod.GET, url = "/search/lists")
	public List<ItemList> searchItemListsByName( @RequestParameter("name") Optional<String> name){
		return todoService.searchItemListsByName(name.orElse(""));
	}
	
	@RequestHandler(method = HttpMethod.GET, url = "/search/list/{id}/items")
	public List<Item> searchItemFromListByName( @PathVariable("id") String itemListId,
			@RequestParameter("name") Optional<String> name){
		return todoService.searchItemsFromListByName(itemListId, name.orElse(""));
	}
	
}
