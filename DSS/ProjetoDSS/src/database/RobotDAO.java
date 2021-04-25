package database;

import Modelo.GPS;
import Modelo.Robot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RobotDAO implements Map<String, Robot> {
    private static RobotDAO st = null;

    /**
     * Construtor privado relativo à base de dados do Robot
     */
    private RobotDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS Robot (" +
                    "codRobot varchar(6) NOT NULL," +
                    "localizacao_x INT NOT NULL," +
                    "localizacao_y INT NOT NULL," +
                    "disponibilidade TINYINT NOT NULL," +
                    "codPalete varchar(6) NULL," +
                    "PRIMARY KEY (codRobot))";
            stm.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Implementação do padrão Singleton (st)
     * @return Instância única desta classe
     */
    public static RobotDAO getInstance() {
        if (RobotDAO.st == null) RobotDAO.st = new RobotDAO();
        return RobotDAO.st;
    }

    /**
     * Implementação do cálculo do tamanho da instância, neste caso referente ao tamanho da tabela
     * @return Número de Robots na base de dados
     */
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Robot")) {
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    /**
     * Implementação do método que averigua se a base de dados não tem nenhum Robot
     * @return Valor lógico da afirmação (true se não existirem Robot)
     */
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /**
     * Implementação do método que averigua se existe um dado Robot na base de dados através do seu código identificativo.
     * @param key Código do Robot a averiguar
     * @return Valor lógico da afirmação (true se existir a key que procuramos)
     */
    public boolean containsKey(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT codRobot FROM Robot WHERE codRobot='" + key.toString() + "'")) {
            r = rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ;
        return r;
    }

    /**
     * Implementação do método que averigua se existe um dado Robot na base de dados através do seu valor.
     * @param value Robot a averiguar
     * @return Valor lógico da afirmação (true se exisitir o Robot que procuramos)
     */
    @Override
    public boolean containsValue(Object value) {
        Robot r = (Robot) value;
        return this.containsKey(r.getCod());
    }

    /**
     * Implementação do método que devolve um Robot a partir do seu código.
     * @param key Código do Robot que pretendemos obter
     * @return Robot pretendido
     */
    @Override
    public Robot get(Object key) {
        Robot r = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Robot WHERE codRobot = '" + key.toString() + "' ")) {
            if (rs.next()) {
                r = new Robot(rs.getString("codRobot"), new GPS(rs.getInt("localizacao_x"), rs.getInt("localizacao_y")), rs.getInt("disponibilidade") == 1, rs.getString("codPalete"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r;
    }

    /**
     * Implementação do método que insere na base de dados um dado Robot
     * @param key Código do Robot a inserir na base de dados
     * @param r1 Valor do Robot a inserir na base de dados
     * @return null
     */
    @Override
    public Robot put(String key, Robot r1) {
        Robot r2 = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            int d;
            if (r1.isAvailable()) d = 1;
            else d = 0;
            if (r1.getCodPalete() != null) {
                stm.executeUpdate("INSERT INTO Robot " +
                        "VALUES ('" + r1.getCod() + "', " +
                        "'" + r1.getLocalizacao().getX() + "', " +
                        "'" + r1.getLocalizacao().getY() + "', " +
                        "'" + d + "', " +
                        "'" + r1.getCodPalete() + "')" +
                        "ON DUPLICATE KEY UPDATE localizacao_x=VALUES(localizacao_x), localizacao_y=VALUES(localizacao_y), disponibilidade=VALUES(disponibilidade),codPalete=VALUES(codPalete)");
            } else {
                stm.executeUpdate("INSERT INTO Robot VALUES ('" + r1.getCod() + "', '" +
                        r1.getLocalizacao().getX() + "', '" +
                        r1.getLocalizacao().getY() + "', '" +
                        d + "', NULL )" +
                        "ON DUPLICATE KEY UPDATE localizacao_x=VALUES(localizacao_x), localizacao_y=VALUES(localizacao_y), disponibilidade=VALUES(disponibilidade),codPalete=VALUES(codPalete)");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r2;
    }

    /**
     * Implementação do método que remove da base de dados um dado Robot
     * @param key Código do Robot que pretendemos remover
     * @return Robot removido
     */
    @Override
    public Robot remove(Object key) {
        Robot r = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();) {
            stm.executeUpdate("DELETE FROM Robot WHERE codRobot = '" + key.toString() + "'");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r;
    }

    /**
     * Implementação do método que adiciona um conjunto de Robot à base de dados
     * @param robots Conjunto de Robot a adicionar
     */
    @Override
    public void putAll(Map<? extends String, ? extends Robot> robots) {
        for (Robot r : robots.values()) this.put(r.getCod(), r);
    }

    /**
     * Implementação do método que elimina todos os dados referentes a Robots da base de dados, isto é, limpa a sua tabela
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.execute("TRUNCATE Robot");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Implementação do método que devolve todos os códigos de Robot existentes na base de dados
     * @return Conjunto com os códigos de Robot
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT codRobot FROM Robot");) {
            while (rs.next()) {
                String idt = rs.getString("codRobot");
                res.add(idt);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    /**
     * Implementação do método que devolve todos os valores de Robot existentes na base de dados
     * @return Coleção com os Robots
     */
    @Override
    public Collection<Robot> values() {
        Collection<Robot> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT codRobot FROM Robot")) {
            while (rs.next()) {
                String idt = rs.getString("codRobot");
                Robot r = this.get(idt);
                res.add(r);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    /**
     * Método não implementado, deveria retornar um conjunto com as Entrys do map referente à base de dados
     * contendo o código e o valor de cada Robot existente
     * @return Nothing
     */
    @Override
    public Set<Entry<String, Robot>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Robot>> entrySet() not implemented!");
    }

    /**
     * Implementação de um método que faz a povoação inicial da tabela dos Robot
     */
    public static void povoa() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "INSERT INTO Robot (codRobot, " +
                    "localizacao_x, " +
                    "localizacao_y," +
                    "disponibilidade," +
                    "codPalete)" +
                    "VALUES ('R01',5,5,1,NULL)," +
                    "('R02',10,5,1,NULL)," +
                    "('R03',25,0,1,NULL)," +
                    "('R04',15,0,1,NULL)," +
                    "('R05',20,5,1,NULL)" +
                    "ON DUPLICATE KEY UPDATE codRobot=VALUES(codRobot)";
            stm.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


}
