package dream.store;

import dream.model.Model;
import dream.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStoreUser implements Store{

    private static final Logger logger = Logger.getLogger(PsqlStorePost.class);
    private final BasicDataSource pool = new BasicDataSource();


    private PsqlStoreUser() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("src/main/resources/db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            logger.error("Error: ", e);
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            logger.error("Error: ", e);
        }

        pool.setDriverClassName("org.postgresql.Driver");
        pool.setUrl("jdbc:postgresql://127.0.0.1:5433/DreamJob");
        pool.setUsername("postgres");
        pool.setPassword("PassworD1");
//        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
//        pool.setUrl(cfg.getProperty("jdbc.url"));
//        pool.setUsername(cfg.getProperty("jdbc.username"));
//        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStoreUser();
    }

    public static PsqlStoreUser instOf() {
        return (PsqlStoreUser) Lazy.INST;
    }

    @Override
    public Collection<User> findAll() {
        List<User> models = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    models.add(new User(it.getInt("id"), it.getString("name"),
                            it.getString("email"), it.getString("password")));
                }
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return models;
    }

    @Override
    public void save(Model model) {
        User user = (User) model;
        if (user.getId() == 0) {
            create(user);
        } else {
            update(user);
        }
    }

    private Model create(User user) {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO users (name, email, password) VALUES (?, ?, ?);",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Exception: ", e);
        }
        return user;
    }

    private Model update(User user) {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement("UPDATE users SET name = ?",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.executeUpdate();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return user;
    }

    @Override
    public Model findById(int id) {
        User user = null;
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users where id=?");
            ps.setInt(1, id);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    user = new User(rs.getInt("id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("password"));
                }
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return user;
    }

    public boolean delete(int id) {
        boolean flag = false;
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =  cn.prepareStatement("DELETE FROM users where id=?");
            ps.setInt(1, id);
            ps.executeQuery();
            flag = ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return flag;
    }

    public User findByEmail(String email) {
        User user = null;
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =  cn.prepareStatement("SELECT * FROM users where email=?");
            ps.setString(1, email);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    user = new User(rs.getInt("id"), rs.getString("name"),
                            rs.getString("email"), rs.getString("password"));
                }
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return user;
    }

    public static void main(String[] args) {
//        PsqlStoreUser store = new PsqlStoreUser();

//        System.out.println("delete from 2 to 3");
//        for(int i = 1; i < 10; i++){
//            store.delete(i);
//        }
        User user1 = new User("name1", "a@mail.ru", "1");
        User user2 = new User("name2", "b@mail.ru", "2");
//        store.save(user1);
//        store.save(user2);
        System.out.println("FIND EMAIL");
        System.out.println(PsqlStoreUser.instOf().findByEmail("a@mail.ru"));
        System.out.println();

        System.out.println("FIND ALL");
        PsqlStoreUser.instOf().findAll().forEach(System.out::println);
        System.out.println();

        System.out.println("FIND BY ID");
        System.out.println(PsqlStoreUser.instOf().findById(1));
        System.out.println();

//        System.out.println("UPDATE");
//        user2.setName("upd2");
//        store.save(user2);
//        store.findAll().forEach(System.out::println);
    }
}
