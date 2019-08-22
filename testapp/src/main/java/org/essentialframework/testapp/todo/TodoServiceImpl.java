package org.essentialframework.testapp.todo;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.essentialframework.core.annotation.Service;
import org.essentialframework.testapp.todo.domain.Item;
import org.essentialframework.testapp.todo.domain.ItemList;
import org.essentialframework.testapp.todo.domain.ItemListRepository;
import org.essentialframework.testapp.todo.domain.ItemRepository;

@Service
public class TodoServiceImpl implements TodoService {

	private ItemListRepository itemListRepository;
	private ItemRepository itemRepository;
	
	private ItemListValidator itemListValidator;
	private ItemValidator itemValidator;
	
	public TodoServiceImpl(ItemListRepository itemListRepository, ItemRepository itemRepository) {
		this.itemListRepository = itemListRepository;
		this.itemRepository = itemRepository;	
	}
	
	public void setItemListValidator(ItemListValidator itemListValidator) {
		this.itemListValidator = itemListValidator;
	}

	public void setItemValidator(ItemValidator itemValidator) {
		this.itemValidator = itemValidator;
	}

	
	@Override
	public List<ItemList> getItemLists() {
		return itemListRepository.findAll();
	}

	@Override
	public List<Item> getItems(String itemListId) {
		return itemRepository.findByItemListId(itemListId);
	}

	@Override
	public void markAsCompleted(String itemId) {
		final Item item = itemRepository.findById(itemId);
		if(item != null) {
			item.setCompleted(true);
			itemRepository.save(item);
		}
	}

	@Override
	public void markAsStarred(String itemId) {
		final Item item = itemRepository.findById(itemId);
		if(item != null) {
			item.setStarred(true);
			itemRepository.save(item);
		}
	}
	
	@Override
	public List<Item> getAllStarredItems() {
		return itemRepository.findAll().stream()
			.filter(item -> item.isStarred())
			.collect(Collectors.toList());
	}
	
	@Override
	public List<Item> getStarredItemsFromList(String itemListId) {
		return itemRepository.findByItemListId(itemListId).stream()
			.filter(item -> item.isStarred())
			.collect(Collectors.toList());
	}

	@Override
	public List<Item> getAllCompletedItems() {
		return itemRepository.findAll().stream()
			.filter(item -> item.isCompleted())
			.collect(Collectors.toList());
	}

	@Override
	public List<Item> getCompletedItemsFromList(String itemListId) {
		return itemRepository.findByItemListId(itemListId).stream()
			.filter(item -> item.isCompleted())
			.collect(Collectors.toList());
	}
	
	@Override
	public void save(ItemList itemList) throws TodoServiceException {
		if(itemListValidator != null) {
			itemListValidator.validate(itemList);
		}
		itemListRepository.save(itemList);
	}

	@Override
	public void save(Item item) throws TodoServiceException {
		if(itemValidator != null) {
			itemValidator.validate(item);
		}
		itemRepository.save(item);
	}

	@Override
	public List<ItemList> searchItemListsByName(String name) {
		return itemListRepository.findAll().stream()
			.filter(item -> 
				Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(item.getName()).find())
			.collect(Collectors.toList());	
	}

	@Override
	public List<Item> searchItemsFromListByName(String itemListId, String name) {
		return itemRepository.findByItemListId(itemListId).stream()
			.filter(item -> 
				Pattern.compile(Pattern.quote(name), Pattern.CASE_INSENSITIVE).matcher(item.getName()).find())
			.collect(Collectors.toList());	
	}
	
	@Override
	public void deleteItemList(String itemListId) {
		itemListRepository.delete(itemListId);
	}

	@Override
	public void deleteItem(String itemId) {
		itemRepository.delete(itemId);
	}
	
	private static interface ItemListValidator {
		default void validate(ItemList itemList) throws TodoServiceException {
			//no-op, implement if needed
		}
	}
	
	private static interface ItemValidator {
		default void validate(Item item) throws TodoServiceException {
			//no-op, implement if needed
		}
	}

}
