package database;

import Modelo.Palete;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PaleteDAO implements Map<String, Palete> {
    private static PaleteDAO st = null;

    /**
     * Construtor privado relativo à base de dados da Palete
     */
    private PaleteDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS `projetoDSS`.`Palete` (\n" +
                    "        `QRCode` VARCHAR(6) NOT NULL,\n" +
                    "        `Material` VARCHAR(10) NOT NULL,\n" +
                    "        `Prateleira_codPrateleira` VARCHAR(6) NULL,\n" +
                    "        `Robot_codRobot` VARCHAR(6) NULL,\n" +
                    "        PRIMARY KEY (QRCode),\n" +
                    "        FOREIGN KEY (Prateleira_codPrateleira) REFERENCES Prateleira(codPrateleira),\n" +
                    "        FOREIGN KEY (Robot_codRobot) REFERENCES Robot(codRobot))";
            stm.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Implementação do padrão Singleton (st)
     * @return Instância única desta classe
     */
    public static PaleteDAO getInstance() {
        if (PaleteDAO.st == null) PaleteDAO.st = new PaleteDAO();
        return PaleteDAO.st;
    }

    /**
     * Implementação do cálculo do tamanho da instância, neste caso referente ao tamanho da tabela
     * @return Número de Palete na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM Palete")) {
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return i;
    }

    /**
     * Implementação do método que averigua se a base de dados não tem nenhuma Palete
     * @return Valor lógico da afirmação (true se não existirem Palete)
     */
    @Override
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /**
     * Implementação do método que averigua se existe uma dada Palete na base de dados através do seu código identificativo.
     * @param key Código da Palete a averiguar
     * @return Valor lógico da afirmação (true se existir a key que procuramos)
     */
    @Override
    public boolean containsKey(Object key) {
        boolean r = false;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT QRCode FROM Palete WHERE QRCode = '" + key.toString() + "' ")) {
            r = rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return r;
    }

    /**
     * Implementação do método que averigua se existe uma dada Palete na base de dados através do seu valor.
     * @param value Palete a averiguar
     * @return Valor lógico da afirmação (true se exisitir a Palete que procuramos)
     */
    @Override
    public boolean containsValue(Object value) {
        Palete p = (Palete) value;
        return this.containsKey(p.getCodPalete());
    }

    /**
     * Implementação do método que devolve uma Palete a partir do seu código.
     * @param key Código da Palete que pretendemos obter
     * @return Palete pretendida
     */
    @Override
    public Palete get(Object key) {
        Palete p = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Palete WHERE QRCode = '" + key.toString() + "' ")) {
            if (rs.next()) {
                if (rs.getString("Prateleira_codPrateleira") == null)
                    p = new Palete(rs.getString("QRCode"), rs.getString("Robot_codRobot"), rs.getString("Material"));
                else
                    p = new Palete(rs.getString("QRCode"), rs.getString("Prateleira_codPrateleira"), rs.getString("Material"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return p;
    }

    /**
     * Implementação do método que insere na base de dados uma dada Palete
     * @param key Código da Palete a inserir na base de dados
     * @param value Valor da Palete a inserir na base de dados
     * @return null
     */
    @Override
    public Palete put(String key, Palete value) {
        Palete res = null;

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            if (value.getLoc().startsWith("R")) {
                stm.executeUpdate("INSERT INTO Palete VALUES ('" + value.getCodPalete() + "', '" + value.getM() + "', NULL , '" + value.getLoc() + "')" +
                        "ON DUPLICATE KEY UPDATE Prateleira_codPrateleira=VALUES(Prateleira_codPrateleira), Robot_codRobot=VALUES(Robot_codRobot)");
            } else {
                stm.executeUpdate("INSERT INTO Palete VALUES ('" + value.getCodPalete() + "', '" + value.getM() + "', '" + value.getLoc() + "' , NULL)" +
                        "ON DUPLICATE KEY UPDATE Prateleira_codPrateleira=VALUES(Prateleira_codPrateleira), Robot_codRobot=VALUES(Robot_codRobot)");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return res;
    }

    /**
     * Implementação do método que remove da base de dados uma dada Palete
     * @param key Código da Palete que pretendemos remover
     * @return Palete removida
     */
    @Override
    public Palete remove(Object key) {
        Palete p = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM Palete WHERE QRCode = '" + key.toString() + "' ");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return p;
    }

    /**
     * Implementação do método que adiciona um conjunto de Palete à base de dados
     * @param paletes Conjunto de Palete a adicionar
     */
    @Override
    public void putAll(Map<? extends String, ? extends Palete> paletes) {
        for (Palete p : paletes.values())
            this.put(p.getCodPalete(), p);
    }

    /**
     * Implementação do método que elimina todos os dados referentes a Palete da base de dados, isto é, limpa a sua tabela
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.execute("TRUNCATE Palete");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Implementação do método que devolve todos os códigos de Palete existentes na base de dados
     * @return Conjunto com os códigos de Palete
     */
    @Override
    public Set<String> keySet() {
        Set<String> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT QRCode FROM Palete")) {
            while (rs.next()) {
                String idt = rs.getString("QRCode");
                res.add(idt);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    /**
     * Implementação do método que devolve todos os valores de Palete existentes na base de dados
     * @return Coleção com as Palete
     */
    @Override
    public Collection<Palete> values() {
        Collection<Palete> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT QRCode FROM Palete")) {
            while (rs.next()) {
                String idt = rs.getString("QRCode");
                Palete p = this.get(idt);
                res.add(p);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return res;
    }

    /**
     * Método não implementado, deveria retornar um conjunto com as Entrys do map referente à base de dados
     * contendo o código e o valor de cada Palete existente
     * @return Nothing
     */
    @Override
    public Set<Entry<String, Palete>> entrySet() {
        throw new NullPointerException("public Set<Map.Entry<String,Palete>> entrySet() not implemented!");
    }
}
