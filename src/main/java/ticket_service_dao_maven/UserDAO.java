package ticket_service_dao_maven;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UserDAO {

    private EntityManager entityManager;

    public UserDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void saveUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user); // Persist the user entity
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback the transaction in case of an error
            }
            throw e;
        }
    }

    public User getUserById(int id) {
        return entityManager.find(User.class, id); // Find a user by ID
    }

    public void updateUser(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user); // Merge the changes to the user entity
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void deleteUserById(int id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            User user = entityManager.find(User.class, id);
            if (user != null) {
                entityManager.remove(user); // Remove the user entity
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    public void updateUserAndTickets(User user, List<Ticket> tickets) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user); // Update the user

            for (Ticket ticket : tickets) {
                entityManager.merge(ticket); // Update each ticket
            }

            transaction.commit(); // Commit the transaction
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback(); // Rollback the transaction in case of an error
            }
            throw e;
        }
    }

}

