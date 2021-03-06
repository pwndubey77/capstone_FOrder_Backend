package org.upgrad.services;

import org.upgrad.models.Item;

import java.util.List;

public interface ItemService {

    List<Item> getItemByPopularity(int restaurantId);
    Item getItemById(int id);

    List<Item> getTop5ItemsByPopularity(int restaurantId) ;
}
