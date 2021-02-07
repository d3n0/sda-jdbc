import java.sql.*;

public class Main {
    private static final String url = "jdbc:h2:mem:movies";
    private static final String user = "sa";
    private static final String password = "";
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
        try (Connection conn = DriverManager.getConnection(url,user,password)){

            MovieDAO movieDAO = new MovieDAOImpl(conn);
            movieDAO.createTable();

            createMovie("Interstellar","Movie", 1999, conn);
            createMovie("100", "Science", 2010, conn);
            createMovie("Another Movie", "Action", 2021, conn);
            System.out.println("Pries:");
            printMovies(conn);
            try (PreparedStatement updateMovie = conn.prepareStatement("UPDATE MOVIES SET title = ? WHERE id = ?")){
                updateMovie.setString(1, "Interstellar 2");
                updateMovie.setInt(2,1);
                updateMovie.execute();
            }
            System.out.println("Po:");
            deleteMovie(2, conn);
            printMovies(conn);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void printMovies(Connection conn) throws SQLException {
        try (Statement selectMovies = conn.createStatement()){
            ResultSet resultSet = selectMovies.executeQuery("SELECT * FROM MOVIES");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                int yearsOfRelease = resultSet.getInt("yearsOfRelease");
                System.out.println("id: " + id + "; title: " + title +"; genre: "+ genre +"; Release Years: "+ yearsOfRelease);
            }
        }
    }
    private static void createMovie(String title, String genre, int yearsOfRelease, Connection conn) {
        try (PreparedStatement insertMovie = conn.prepareStatement("INSERT INTO MOVIES (title, genre, yearsOfRelease) VALUES (?, ?, ?)")){
            insertMovie.setString(1, title);
            insertMovie.setString(2, genre);
            insertMovie.setInt(3, yearsOfRelease);
            insertMovie.execute();
        }catch (SQLException e){
            System.out.println("Nepavyko prideti filmo");
        }
    }
    private static void deleteMovie(int id, Connection conn) {
        try (PreparedStatement deleteMovie = conn.prepareStatement("DELETE FROM MOVIES WHERE id=?")){
            deleteMovie.setInt(1, id);
            deleteMovie.execute();
        }catch (SQLException e){
            System.out.println("Nepavyko prideti filmo");
        }
    }
}