package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface PublicAuction {

    List<Bid> getUserBids(long id);

    List<Item> getUserItems(long id);

    Item getItemByName(String name);

    Item getItemByDescription(String name);

    Map<User, Double> getAvgItemCost();

    Map<Item, Bid> getMaxBidsForEveryItem();

    default List<Bid> getUserActualBids(long id) {
        return Collections.emptyList();
    }

    boolean createUser(User user);

    boolean createItem(Item item);

    boolean createBid(Bid bid);

    boolean deleteUserBids(long id);

    boolean doubleItemsStartPrice(long id);

}
