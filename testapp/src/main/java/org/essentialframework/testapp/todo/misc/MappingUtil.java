package org.essentialframework.testapp.todo.misc;

import java.time.LocalDateTime;

import org.essentialframework.testapp.todo.domain.Item;
import org.essentialframework.testapp.todo.domain.ItemList;

public class MappingUtil {
	
	public static final ItemList map(ItemListDto itemListDto) {
		final ItemList itemList = new ItemList();
		itemList.setName(itemListDto.getName());
		itemList.setDescription(itemListDto.getDescription());
		itemList.setCreatedAt(LocalDateTime.now());
		return itemList;
	}
	
	public static final Item map(ItemDto itemDto) {
		final Item item = new Item();
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setItemListId(itemDto.getItemListId());
		item.setCreatedAt(LocalDateTime.now());
		return item;
	}
	
	private MappingUtil() {}

}
