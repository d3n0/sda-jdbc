import java.sql.*;

public class PirmaUzduotis {

    private static final String URL = "jdbc:h2:mem:theater";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

            MovieDAO movieDAO = new MovieDAOImpl(conn);
            movieDAO.deleteTable();
            movieDAO.createTable();

            movieDAO.createMovie(new Movie("Interstellar", "Fantasy", 2004));
            movieDAO.createMovie(new Movie("100", "Action", 2001));
            movieDAO.createMovie(new Movie("Tony", "Horor", 2002));

            movieDAO.deleteMovie(2);

            try (PreparedStatement updateMovie = conn.prepareStatement("UPDATE MOVIES SET title = ? WHERE id = ?")) {
                updateMovie.setString(1, "A.I.");
                updateMovie.setInt(2, 1);
                updateMovie.execute();
            }

            printMovies(conn);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void printMovies(Connection conn) throws SQLException {
        try (Statement selectMovies = conn.createStatement()) {
            ResultSet resultSet = selectMovies.executeQuery("SELECT * FROM MOVIES");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                System.out.println("id: " + id + "; title: " + title);
            }
        }
    }
}
