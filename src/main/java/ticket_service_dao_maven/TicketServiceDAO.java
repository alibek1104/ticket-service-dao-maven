package ticket_service_dao_maven;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TicketServiceDAO {
    private static final String URL = "jdbc:postgresql://localhost:5434/my_ticket_service_db";
    private static final String USER = "your_username"; // Замените на ваше имя пользователя
    private static final String PASSWORD = "your_password"; // Замените на ваш пароль

    // Метод для соединения с базой данных
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Метод для сохранения пользователя
    public void saveUser(User user) throws SQLException {
        String query = "INSERT INTO public.User (name, creation_date) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getName());
            stmt.setTimestamp(2, user.getCreationDate());
            stmt.executeUpdate();
        }
    }

    // Метод для сохранения билета
    public void saveTicket(Ticket ticket) throws SQLException {
        String query = "INSERT INTO public.Ticket (user_id, ticket_type, creation_date) VALUES (?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setString(2, ticket.getTicketType());
            stmt.setTimestamp(3, ticket.getCreationDate());
            stmt.executeUpdate();
        }
    }

    // Метод для получения пользователя по ID
    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM public.User WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("creation_date"));
            }
            return null;
        }
    }

    // Метод для получения билета по ID
    public Ticket getTicketById(int id) throws SQLException {
        String query = "SELECT * FROM public.Ticket WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ticket(rs.getInt("id"), rs.getInt("user_id"), rs.getString("ticket_type"), rs.getTimestamp("creation_date"));
            }
            return null;
        }
    }

    // Метод для получения всех билетов пользователя по его ID
    public List<Ticket> getTicketsByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM public.Ticket WHERE user_id = ?";
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(new Ticket(rs.getInt("id"), rs.getInt("user_id"), rs.getString("ticket_type"), rs.getTimestamp("creation_date")));
            }
        }
        return tickets;
    }

    // Метод для обновления типа билета
    public void updateTicketType(int ticketId, String newTicketType) throws SQLException {
        String query = "UPDATE public.Ticket SET ticket_type = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newTicketType);
            stmt.setInt(2, ticketId);
            stmt.executeUpdate();
        }
    }

    // Метод для удаления пользователя и его билетов
    public void deleteUserById(int userId) throws SQLException {
        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Начинаем транзакцию

            // Удаление билетов пользователя
            String deleteTicketsQuery = "DELETE FROM public.Ticket WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteTicketsQuery)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            // Удаление пользователя
            String deleteUserQuery = "DELETE FROM public.User WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteUserQuery)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }

            conn.commit(); // Фиксируем транзакцию
        } catch (SQLException e) {
            throw new SQLException("Ошибка при удалении пользователя и его билетов", e);
        }
    }
}
