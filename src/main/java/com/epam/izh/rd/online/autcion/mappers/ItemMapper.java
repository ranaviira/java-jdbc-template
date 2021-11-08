package com.epam.izh.rd.online.autcion.mappers;

import com.epam.izh.rd.online.autcion.entity.Item;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class ItemMapper implements RowMapper<Item> {

    @Override
    public Item mapRow(ResultSet resultSet, int i) throws SQLException {

        Item item = new Item();

        item.setItemId(resultSet.getLong("item_id"));
        item.setBidIncrement(resultSet.getDouble("bid_increment"));
        item.setBuyItNow(resultSet.getBoolean("buy_it_now"));
        item.setDescription(resultSet.getString("description"));
        item.setStartDate(LocalDate.parse(resultSet.getString("start_date")));
        item.setStartPrice(resultSet.getDouble("start_price"));
        item.setStopDate(LocalDate.parse(resultSet.getString("stop_date")));
        item.setTitle(resultSet.getString("title"));
        item.setUserId(resultSet.getLong("user_id"));

        return item;
    }
}
