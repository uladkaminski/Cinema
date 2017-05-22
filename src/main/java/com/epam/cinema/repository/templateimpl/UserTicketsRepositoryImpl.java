package com.epam.cinema.repository.templateimpl;

import com.epam.cinema.repository.UserTicketsRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


public class UserTicketsRepositoryImpl implements UserTicketsRepository {
    private static final String INSERT_USER_TICKET = "INSERT INTO USER_TICKET (user_id, ticket_id) VALUES (?, ?)";
    private static final String GET_TICKETS_ID = "SELECT ticket_id FROM USER_TICKET user WHERE user_id = ?";

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Long> getUserTicketsIds(Long userId) {
        return jdbcTemplate.query(GET_TICKETS_ID, new Object[]{userId},
                ((resultSet, i) -> resultSet.getLong(1)));
    }

    @Override
    public void addUserTicket(Long userId, Long ticketId) {
        jdbcTemplate.update(INSERT_USER_TICKET, userId, ticketId);
    }
}