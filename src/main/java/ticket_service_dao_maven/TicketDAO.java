package ticket_service_dao_maven;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class TicketDAO {

    private EntityManager entityManager;

    public TicketDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveTicket(Ticket ticket) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(ticket); // Persist the ticket entity
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Ticket getTicketById(int id) {
        return entityManager.find(Ticket.class, id); // Find a ticket by ID
    }

    public List<Ticket> getTicketsByUserId(int userId) {
        return entityManager.createQuery("SELECT t FROM Ticket t WHERE t.user.id = :userId", Ticket.class)
                .setParameter("userId", userId)
                .getResultList(); // Find tickets by user ID
    }

    public void updateTicketType(int ticketId, TicketType newTicketType) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Ticket ticket = entityManager.find(Ticket.class, ticketId);
            if (ticket != null) {
                ticket.setTicketType(newTicketType); // Update the ticket type
                entityManager.merge(ticket);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteTicketById(int ticketId) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Ticket ticket = entityManager.find(Ticket.class, ticketId);
            if (ticket != null) {
                entityManager.remove(ticket); // Remove the ticket entity
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}

