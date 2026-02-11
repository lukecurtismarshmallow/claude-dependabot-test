package com.example.demo;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ItemControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    void shouldCreateItem() throws Exception {
        Item item = new Item("test widget", "A test item", 5);

        mockMvc.perform(post("/api/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Widget")))
                .andExpect(jsonPath("$.quantity", is(5)));
    }

    @Test
    void shouldGetAllItems() throws Exception {
        itemRepository.save(new Item("Widget A", "First widget", 3));
        itemRepository.save(new Item("Widget B", "Second widget", 7));

        mockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldGetItemById() throws Exception {
        Item saved = itemRepository.save(new Item("Widget", "A widget", 10));

        mockMvc.perform(get("/api/items/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Widget")))
                .andExpect(jsonPath("$.quantity", is(10)));
    }

    @Test
    void shouldReturn404ForMissingItem() throws Exception {
        mockMvc.perform(get("/api/items/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateItem() throws Exception {
        Item saved = itemRepository.save(new Item("Old Name", "Old desc", 1));
        Item update = new Item("new name", "New desc", 99);

        mockMvc.perform(put("/api/items/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New Name")))
                .andExpect(jsonPath("$.quantity", is(99)));
    }

    @Test
    void shouldDeleteItem() throws Exception {
        Item saved = itemRepository.save(new Item("Delete Me", "To be deleted", 0));

        mockMvc.perform(delete("/api/items/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/items/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSearchItems() throws Exception {
        itemRepository.save(new Item("Blue Widget", "A blue one", 2));
        itemRepository.save(new Item("Red Gadget", "A red one", 4));

        mockMvc.perform(get("/api/items").param("search", "widget"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Blue Widget")));
    }
}
