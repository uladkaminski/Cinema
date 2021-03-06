package com.epam.cinema.repository.impl;

import com.epam.cinema.aspect.CountBookingTickets;
import com.epam.cinema.model.Ticket;
import com.epam.cinema.repository.EventRepository;
import com.epam.cinema.repository.SeatRepository;
import com.epam.cinema.repository.TicketRepository;
import com.epam.cinema.repository.UserRepository;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.H2SequenceMaxValueIncrementer;

import java.sql.Timestamp;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepository {

    private static final String GET_ALL = "SELECT id, event_id, datetime, seat_id, user_id, booked FROM TICKETS";
    private static final String GET_BY_USER_ID = "SELECT id, event_id, datetime, seat_id, user_id, booked FROM TICKETS WHERE USER_ID = ?";
    private static final String GET_BY_EVENT_ID = "SELECT id, event_id, datetime, seat_id, user_id, booked FROM TICKETS WHERE event_id = ?";
    private static final String BOOK_TICKET = "UPDATE TICKETS SET user_id = ?, booked = TRUE WHERE ID = ?";
    private static final String DECLINE_TICKET = "UPDATE TICKETS SET user_id = NULL, booked = FALSE WHERE ID = ?";
    private static final String INSERT_TICKET = "INSERT INTO TICKETS(ID, EVENT_ID, DATETIME, SEAT_ID, USER_ID, BOOKED) VALUES (?,?,?,?,?,?)";
    private JdbcTemplate jdbcTemplate;
    private H2SequenceMaxValueIncrementer ticketIncrementer;

    private UserRepository userRepository;
    private EventRepository eventRepository;
    private SeatRepository seatRepository;

    private final Logger log = Logger.getLogger(TicketRepositoryImpl.class);

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setTicketIncrementer(H2SequenceMaxValueIncrementer ticketIncrementer) {
        this.ticketIncrementer = ticketIncrementer;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setEventRepository(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void setSeatRepository(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public List<Ticket> getAll() {
        log.info("Retrieving all tickets");
        return jdbcTemplate.query(GET_ALL, (resultSet, i) -> new Ticket(
                resultSet.getLong(1),
                eventRepository.getById(resultSet.getLong(2)),
                resultSet.getTimestamp(3).toLocalDateTime(),
                seatRepository.getById(resultSet.getLong(4)),
                userRepository.getById(resultSet.getLong(5)),
                resultSet.getBoolean(6)
        ));
    }

    @Override
    public List<Ticket> getTicketsForUser(Long userId) {
        log.info("Retrieving all tickets for user");

        return jdbcTemplate.query(GET_BY_USER_ID, new Object[]{userId}, (resultSet, i) -> new Ticket(
                resultSet.getLong(1),
                eventRepository.getById(resultSet.getLong(2)),
                resultSet.getTimestamp(3).toLocalDateTime(),
                seatRepository.getById(resultSet.getLong(4)),
                userRepository.getById(resultSet.getLong(5)),
                resultSet.getBoolean(6)
        ));
    }

    @Override
    public List<Ticket> getTicketForEvent(Long eventId) {
        log.info("Retrieving all tickets for event");

        return jdbcTemplate.query(GET_BY_EVENT_ID, new Object[]{eventId}, (resultSet, i) -> new Ticket(
                resultSet.getLong(1),
                eventRepository.getById(resultSet.getLong(2)),
                resultSet.getTimestamp(3).toLocalDateTime(),
                seatRepository.getById(resultSet.getLong(4)),
                userRepository.getById(resultSet.getLong(5)),
                resultSet.getBoolean(6)
        ));
    }

    @Override
    @CountBookingTickets
    public void bookTicketForUser(Ticket ticket, Long userId) {
        log.info("Book ticket for user");

        jdbcTemplate.update(BOOK_TICKET, userId, ticket.getId());
    }

    @Override
    public void declineBookingTicket(Ticket ticket) {
        log.info("Decline ticket booking");

        jdbcTemplate.update(DECLINE_TICKET, ticket.getId());
    }

    @Override
    public void save(Ticket ticket) {
        log.info("Save ticket");

        long id = ticketIncrementer.nextLongValue();
        ticket.setId(id);
        jdbcTemplate.update(INSERT_TICKET,
                id,
                ticket.getEvent().getId(),
                Timestamp.valueOf(ticket.getDateTime()),
                ticket.getSeat().getId(),
                ticket.getUser() == null ? null : ticket.getUser().getId(),
                ticket.isBooked());
    }
}
