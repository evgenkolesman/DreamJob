package dream.store;

import dream.model.Model;
import org.apache.commons.dbcp2.BasicDataSource;
import dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

import org.apache.log4j.Logger;

public class PsqlStorePost implements Store {

    private static final Logger logger = Logger.getLogger(PsqlStorePost.class);
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStorePost() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("src/main/resources/db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            logger.error("Error: ", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            logger.error("Error: ", e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStorePost();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAll() {
        List<Post> models = new ArrayList<>();
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
            ) {
                try (ResultSet it = ps.executeQuery()) {
                    while (it.next()) {
                        models.add(new Post(it.getInt("id"), it.getString("name"),
                                it.getString("description"), it.getTimestamp("created")));
                    }
                }
            } catch (Exception e) {
                logger.error("Exception: ", e);
            }
        return models;
    }

    @Override
    public void save(Model model) {
        Post post = (Post) model;
        if (post.getId() == 0) {
            create(post);
        } else {
            update(post);
        }
    }

    private Model create(Post post) {
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps = cn.prepareStatement(
                    "INSERT INTO post (name, description, created) VALUES (?, ?, ?);",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Exception: ", e);
        }
        return post;
    }

    private Model update(Post post) {
        try (Connection cn = pool.getConnection()) {
                PreparedStatement ps = cn.prepareStatement("UPDATE post SET name = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, post.getName());
                ps.executeUpdate();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        post.setId(id.getInt(1));
                    }
                }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return post;
    }

    @Override
    public Model findById(int id) {
        Post post = null;
        try (Connection cn = pool.getConnection()) {
                PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post where id=?");
                ps.setInt(1, id);
                ps.executeQuery();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        post = new Post(rs.getInt("id"), rs.getString("name"),
                                rs.getString("description"), rs.getTimestamp("created"));
                    }
                }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return post;
    }

    public boolean delete(int id) {
        boolean flag = false;
        try (Connection cn = pool.getConnection()) {
            PreparedStatement ps =  cn.prepareStatement("DELETE FROM post where id=?");
            ps.setInt(1, id);
            ps.executeQuery();
            flag = ps.executeUpdate() > 0;
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return flag;
    }


    public static void main(String[] args) {
        PsqlStorePost store = new PsqlStorePost();

        Post post1 = new Post("name1", "desc1");
        Post post2 = new Post("name2", "desc2");
        Post post3 = new Post("name3", "desc3");

        store.save(post1);
        store.save(post2);
        store.save(post3);


//        System.out.println("delete from 10 to 50");
//        for(int i = 10; i < 50; i++){
//            store.delete(i);
//        }
        System.out.println("FIND ALL");
        store.findAll().forEach(System.out::println);
        System.out.println();

        System.out.println("FIND BY ID");
        System.out.println(store.findById(post3.getId()));
        System.out.println();

        System.out.println("UPDATE");
        post3.setName("upd1");
        post2.setName("Ex1");
        store.save(post3);
        store.save(post2);
        System.out.println(store.findById(post3.getId()));
        System.out.println();
        System.out.println(store.findById(post2.getId()));
    }
}