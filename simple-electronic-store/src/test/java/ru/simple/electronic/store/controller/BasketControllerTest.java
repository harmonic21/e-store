package ru.simple.electronic.store.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.simple.electronic.store.service.BasketService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasketControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BasketController basketController;
    @MockitoBean
    private BasketService basketService;

    @Test
    void addProductToBasketTest() throws Exception {
        doNothing().when(basketService).addProduct(eq(UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c")));
        mvc.perform(put("/basket/add/4409ff9f-153f-43fe-b373-11e059d78b3c"))
                .andExpect(status().isOk());
        verify(basketService, times(1)).addProduct(eq(UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c")));
    }

    @Test
    void deleteProductFromBasketTest() throws Exception {
        var uuid = UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c");
        doNothing().when(basketService).deleteProduct(eq(uuid));
        mvc.perform(put("/basket/delete/4409ff9f-153f-43fe-b373-11e059d78b3c"))
                .andExpect(status().isOk());
        verify(basketService, times(1)).deleteProduct(eq(uuid));
    }

    @Test
    void clearBasketTest() throws Exception {
        var uuid = UUID.fromString("4409ff9f-153f-43fe-b373-11e059d78b3c");
        doNothing().when(basketService).clearBasket(eq(uuid));
        mvc.perform(put("/basket/clear/4409ff9f-153f-43fe-b373-11e059d78b3c"))
                .andExpect(status().isOk());
        verify(basketService, times(1)).clearBasket(eq(uuid));
    }
}