package com.epam.izh.rd.online.autcion.repository;

import com.epam.izh.rd.online.autcion.entity.Bid;
import com.epam.izh.rd.online.autcion.entity.Item;
import com.epam.izh.rd.online.autcion.entity.User;
import com.epam.izh.rd.online.autcion.mappers.BidMapper;
import com.epam.izh.rd.online.autcion.mappers.ItemMapper;
import com.epam.izh.rd.online.autcion.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplatePublicAuction implements PublicAuction {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplatePublicAuction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Bid> getUserBids(long id) {
        String sqlQuery = "SELECT * FROM bids WHERE user_id=?;";
        return jdbcTemplate.query(sqlQuery, new Object[]{id}, new BidMapper());
    }

    @Override
    public List<Item> getUserItems(long id) {
        String sqlQuery = "SELECT * FROM items WHERE user_id=?;";
        return jdbcTemplate.query(sqlQuery, new Object[]{id}, new ItemMapper());
    }

    @Override
    public Item getItemByName(String name) {
        String sqlQuery = "SELECT * FROM items WHERE title=?;";
        return jdbcTemplate.queryForObject(sqlQuery, new Object[]{name}, new ItemMapper());
    }

    @Override
    public Item getItemByDescription(String name) {
        String sqlQuery = "SELECT * FROM items WHERE description=?;";
        return jdbcTemplate.queryForObject(sqlQuery, new Object[]{name}, new ItemMapper());
    }

    @Override
    public Map<User, Double> getAvgItemCost() {
        Map<User, Double> mapUserDouble = new HashMap<>();

        String sqlQueryUsers = "SELECT * FROM users;";
        List<User> usersList = jdbcTemplate.query(sqlQueryUsers, new UserMapper());
        if (!(usersList.isEmpty())) {
            for (User user : usersList) {
                String sqlQueryItems = "SELECT AVG(start_price) FROM items WHERE user_id=?;";
                Double avgPriceItemsByUser = jdbcTemplate.queryForObject(sqlQueryItems, new Object[]{user.getUserId()}, Double.class);
                if (avgPriceItemsByUser != null) {
                    mapUserDouble.put(user, avgPriceItemsByUser);
                }
            }
            return mapUserDouble;
        }
        return null;
    }

    @Override
    public Map<Item, Bid> getMaxBidsForEveryItem() {

        Map<Item, Bid> mapItemBid = new HashMap<>();
        String sqlQueryItems = "SELECT * FROM items;";
        List<Item> itemList = jdbcTemplate.query(sqlQueryItems, new ItemMapper());

        if (!(itemList.isEmpty())) {
            for (Item item : itemList) {
                String sqlQueryBids = "SELECT * FROM bids WHERE bid_value = (SELECT MAX(bid_value) FROM bids WHERE item_id = ?);";
                Bid bid = jdbcTemplate.query(sqlQueryBids, new Object[]{item.getItemId()}, new BidMapper()).stream().findAny().orElse(null);
                if (bid != null) {
                    mapItemBid.put(item, bid);
                }
            }
            return mapItemBid;
        }
        return null;
    }

    @Override
    public boolean createUser(User user) {
        if (user != null) {
            String sqlQuery = "INSERT into users values(?, ?, ?, ?, ?);";
            jdbcTemplate.update(sqlQuery,
                    user.getUserId(),
                    user.getBillingAddress(),
                    user.getFullName(),
                    user.getLogin(),
                    user.getPassword());
            return true;
        }
        return false;
    }

    @Override
    public boolean createItem(Item item) {
        if (item != null) {
            String sqlQuery = "INSERT into items values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
            jdbcTemplate.update(sqlQuery,
                    item.getItemId(),
                    item.getBidIncrement(),
                    item.getBuyItNow(),
                    item.getDescription(),
                    item.getStartDate(),
                    item.getStartPrice(),
                    item.getStopDate(),
                    item.getTitle(),
                    item.getUserId());
            return true;
        }
        return false;
    }

    @Override
    public boolean createBid(Bid bid) {
        if (bid != null) {
            String sqlQuery = "INSERT into bids values(?, ?, ?, ?, ?);";
            jdbcTemplate.update(sqlQuery,
                    bid.getBidId(),
                    bid.getBidDate(),
                    bid.getBidValue(),
                    bid.getItemId(),
                    bid.getUserId());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUserBids(long id) {
        if (id > 0) {
            String sqlQuery = "DELETE FROM bids WHERE user_id = ?;";
            jdbcTemplate.update(sqlQuery, id);
            return true;
        }
        return false;
    }

    @Override
    public boolean doubleItemsStartPrice(long id) {
        List<Item> itemList = getUserItems(id);
        if (!(itemList.isEmpty())) {
            for (Item list : itemList) {
                String sqlQuery = "UPDATE items SET start_price=start_price*2 WHERE item_id=?;";
                jdbcTemplate.update(sqlQuery, list.getItemId());
            }
            return true;
        }
        return false;
    }
}
