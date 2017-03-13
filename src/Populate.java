import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Populate {

    public static void main(String args[]) {
        Populate t = new Populate();
        t.run();
    }

    public void run() {
        Connection con = null;
        try {
            con = openConnection();
            Logger.getLogger(getClass().getName()).log(Level.INFO, "DB connected successfully");
            // populateMoviesTable(con);
            // populateMovieGenresTable(con);
            // populateMovieDirectorTable(con);
            // populateMovieActorTable(con);
            // populateTagTable(con)
            // populateMovieTagTable(con);
            // populateUserTable(con);
            // populateUserRatedMovieTable(con);
            
        } catch (SQLException e) {
            System.err.println("Errors occurs when communicating with the database server: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Cannot find the database driver");
        } finally {
            // Never forget to close database connection 
            closeConnection(con);
            Logger.getLogger(getClass().getName()).log(Level.INFO, "DB closed successfully");
        }
    }


    private void populateMoviesTable(Connection con) {
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\movies.dat"))) {
            br.readLine();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Movies VALUES("
                        + "?, ?, ?, "  // mid, title, imdbID
                        + "?, ?, ?, "  // spanishTitle, imdbPictureURL, year
                        + "?, ?, ?, "  // rtID, rtAllCriticsRating, rtAllCriticsNumReviews
                        + "?, ?, ?, "  // rtAllCrtiticsNumFresh, rtAllCrticisNumRotten,rtAllCriticsScore
                        + "?, ?, ?, "  // rtTopCriticsRating, rtTopCriticsNumReviews, rtTopCriticsNumFresh
                        + "?, ?, ?, "  // rtTopCriticsNumRotten, rtTopCriticsScore, rtAudienceRating
                        + "?, ?, ?, "  // rtAudienceNumRating, rtAudienceScore, rtPictureURL
                        + "'x')" // country
                        )) {
                    // mid, title, imdbID
                    stmt.setInt(1, Integer.parseInt(columnDetail[0]));
                    setString(stmt, 2, columnDetail[1]);
                    setString(stmt, 3, columnDetail[2]);
                    
                    // spanishTitle, imdbPictureURL, year
                    setString(stmt, 4, columnDetail[3]);
                    setString(stmt, 5, columnDetail[4]);
                    setInt(stmt, 6, columnDetail[5]);
                    
                    // rtID, rtAllCriticsRating, rtAllCriticsNumReviews
                    setString(stmt, 7, columnDetail[6]);
                    setDouble(stmt, 8, columnDetail[7]);
                    setInt(stmt, 9, columnDetail[8]);
                    
                    // rtAllCrtiticsNumFresh, rtAllCrticisNumRotten,rtAllCriticsScore
                    setInt(stmt, 10, columnDetail[9]);
                    setInt(stmt, 11, columnDetail[10]);
                    setDouble(stmt, 12, columnDetail[11]);
                    
                    // rtTopCriticsRating, rtTopCriticsNumReviews, rtTopCriticsNumFresh
                    setDouble(stmt, 13, columnDetail[12]);
                    setInt(stmt, 14, columnDetail[13]);
                    setInt(stmt, 15, columnDetail[14]);
                    
                    // rtTopCriticsNumRotten, rtTopCriticsScore, rtAudienceRating
                    setInt(stmt, 16, columnDetail[15]);
                    setDouble(stmt, 17, columnDetail[16]);
                    setDouble(stmt, 18,columnDetail[17]);
                    
                    // rtAudienceNumRating, rtAudienceScore, rtPictureURL
                    setInt(stmt, 19, columnDetail[18]);
                    setDouble(stmt, 20, columnDetail[19]);
                    setString(stmt, 21, columnDetail[20]);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try (BufferedReader br1 = new BufferedReader(new FileReader("D:\\movie_countries.dat"))) {

            br1.readLine();
            String sCurrentLine1;
            int line = 0;
            while ((sCurrentLine1 = br1.readLine()) != null) {
                line++;
                String[] columnDetail1 = sCurrentLine1.split("\t");
                try (PreparedStatement stmt1 = con.prepareStatement("UPDATE Movies SET country = ? WHERE mid = ?")) {
                    if (columnDetail1.length != 2) {
                        stmt1.setNull(1, Types.VARCHAR);
                    } else {
                        stmt1.setString(1, columnDetail1[1]);
                    }
                    stmt1.setInt(2, Integer.parseInt(columnDetail1[0]));
                    stmt1.executeUpdate();   
                } catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
           
        }
    }
    
    
     private void populateMovieGenresTable(Connection con) {

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\movie_genres.dat"))) {
            br.readLine();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Movie_Genres VALUES(?, ?)")) { 
                    stmt.setInt(1, Integer.parseInt(columnDetail[0]));
                    setString(stmt, 2, columnDetail[1]);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
     
    private void populateMovieDirectorTable(Connection con) {

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\movie_directors.dat"))) {
            br.readLine();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Movie_Directors VALUES(?, ?, ?)")) { 
                    stmt.setInt(1, Integer.parseInt(columnDetail[0]));
                    setString(stmt, 2, columnDetail[1]);
                    setString(stmt, 3, columnDetail[2]);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
     } 
    
    private void populateMovieActorTable(Connection con) {

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\movie_actors.dat"))) {
            br.readLine();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Movie_Actors VALUES(?, ?, ?, ?)")) { 
                    stmt.setInt(1, Integer.parseInt(columnDetail[0]));
                    setString(stmt, 2, columnDetail[1]);
                    setString(stmt, 3, columnDetail[2]);
                    setInt(stmt, 1, columnDetail[3]);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
     } 
    
     private void populateUserRatedMovieTable(Connection con) {

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\user_ratedmovies.dat"))) {
            br.readLine();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO User_RatedMovie_Timedate VALUES(?, ?, ?, ?)")) { 
                    stmt.setInt(1, Integer.parseInt(columnDetail[0]));
                    stmt.setInt(2, Integer.parseInt(columnDetail[1]));
                    stmt.setDouble(3, Double.parseDouble(columnDetail[2]));
                    stmt.setDate(4, Date.valueOf(
                            new StringBuilder().append(columnDetail[5])
                                    .append("-").append(columnDetail[4])
                                    .append("-").append(columnDetail[3])
                                    .toString()));
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
     } 
     
      private void populateTagTable(Connection con) {

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\tags.dat"))) {
            br.readLine();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Tags VALUES(?, ?)")) { 
                    stmt.setInt(1, Integer.parseInt(columnDetail[0]));
                    setString(stmt, 2, columnDetail[1]);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
      
       private void populateMovieTagTable(Connection con) {

        try (BufferedReader br = new BufferedReader(new FileReader("D:\\movie_tags.dat"))) {
            br.readLine();
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                try (PreparedStatement stmt = con.prepareStatement(
                        "INSERT INTO Movie_Tags VALUES(?, ?, ?)")) { 
                    stmt.setInt(1, Integer.parseInt(columnDetail[0]));
                    stmt.setInt(2, Integer.parseInt(columnDetail[1]));
                    stmt.setInt(3, Integer.parseInt(columnDetail[2]));
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
       
    private void setString(PreparedStatement stmt, int idx, String str) throws SQLException {
        if (str.equals("\\N")) {
            stmt.setNull(idx, Types.VARCHAR);
        } else {
            stmt.setString(idx, str);
        }
    }
    
    private void setInt(PreparedStatement stmt, int idx, String str) throws SQLException {
        if (str.equals("\\N")) {
            stmt.setNull(idx, Types.INTEGER);
        } else {
            stmt.setInt(idx, Integer.parseInt(str));
        }
    }
    
    private void setDouble(PreparedStatement stmt, int idx, String str) throws SQLException {
        if (str.equals("\\N")) {
            stmt.setNull(idx, Types.DOUBLE);
        } else {
            stmt.setDouble(idx, Double.parseDouble(str));
        }
    }
    
    private void populateUserTable(Connection con) {
        try (BufferedReader br = new BufferedReader(new FileReader("D:\\user_ratedmovies.dat"))) {
            br.readLine();
            String sCurrentLine;
            Set<String> inserted = new HashSet<>();
            while ((sCurrentLine = br.readLine()) != null) {
                String[] columnDetail = sCurrentLine.split("\t");
                String userid = columnDetail[0];
                if (inserted.contains(userid)) {
                    continue;
                }
                try (PreparedStatement stmt = con.prepareStatement("INSERT INTO ImdbUser VALUES (?)")) {
                    stmt.setString(1, userid);
                    stmt.executeUpdate();
                    inserted.add(userid);
                } 
                catch (SQLException ex) {
                    Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException(ex);
                } 
            }
        } catch (IOException ex) {
            Logger.getLogger(Populate.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    private Connection openConnection() throws SQLException, ClassNotFoundException {
        // Load the Oracle database driver 
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

        String host = "localhost";
        String port = "1521";
        String dbName = "xe"; //"coen280";
        String userName = "temp";
        String password = "temp";

        // Construct the JDBC URL 
        String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
        return DriverManager.getConnection(dbURL, userName, password);
    }

    /**
     * Close the database connection
     *
     * @param con
     */
    private void closeConnection(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            System.err.println("Cannot close connection: " + e.getMessage());
        }
    }
}
          