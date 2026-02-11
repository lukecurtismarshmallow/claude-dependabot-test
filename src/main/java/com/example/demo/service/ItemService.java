package com.example.demo.service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.commons.text.WordUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        Preconditions.checkNotNull(id, "Item ID must not be null");
        return itemRepository.findById(id);
    }

    public Item createItem(Item item) {
        Preconditions.checkNotNull(item, "Item must not be null");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(item.getName()), "Item name must not be empty");

        item.setName(WordUtils.capitalizeFully(item.getName()));
        return itemRepository.save(item);
    }

    public Optional<Item> updateItem(Long id, Item updated) {
        return itemRepository.findById(id).map(existing -> {
            if (!Strings.isNullOrEmpty(updated.getName())) {
                existing.setName(WordUtils.capitalizeFully(updated.getName()));
            }
            if (updated.getDescription() != null) {
                existing.setDescription(updated.getDescription());
            }
            existing.setQuantity(updated.getQuantity());
            return itemRepository.save(existing);
        });
    }

    public boolean deleteItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Item> searchItems(String query) {
        if (Strings.isNullOrEmpty(query)) {
            return itemRepository.findAll();
        }
        return itemRepository.findByNameContainingIgnoreCase(query);
    }
}
