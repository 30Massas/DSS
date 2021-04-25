package database;

public class DAOconfig {
    static final String USERNAME = "30Massas";                       // Actualizar
    static final String PASSWORD = "Luis180571";                       // Actualizar
    private static final String DATABASE = "projetoDSS";          // Actualizar
    //private static final String DRIVER = "jdbc:mariadb";        // Usar para MariaDB
    private static final String DRIVER = "jdbc:mysql";        // Usar para MySQL
    static final String URL = DRIVER+"://localhost:3306/"+DATABASE;
}
