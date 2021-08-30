package dream.store;

import dream.model.Model;
import org.apache.commons.dbcp2.BasicDataSource;
import dream.model.Candidate;
import dream.model.Post;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class PsqlStore implements Store {

    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new FileReader("db.properties")
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
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
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Model> findAll(String nameClass) {
        List<Model> models = new ArrayList<>();
        if(nameClass.equals("Post")) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    models.add(new Post(it.getInt("id"), it.getString("name"),
                            it.getString("description"), it.getTimestamp("created") ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } }
        if(nameClass.equals("Candidate")) {
            try (Connection cn = pool.getConnection();
                 PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")
            ) {
                try (ResultSet it = ps.executeQuery()) {
                    while (it.next()) {
                        models.add(new Candidate(it.getInt("id"), it.getString("name") ));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return models;

    }

    @Override
    public void save(Model model) {
        if (model.getId() == 0) {
            create(model);
        } else {
            update(model);
        }
    }

    private Model create(Model model) {
        try (Connection cn = pool.getConnection();
        ) {
            if(Objects.equals(model.getClass(), Post.class)) {
                PreparedStatement ps =  cn.prepareStatement("INSERT INTO post (name) VALUES(?);",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getName());
                ps.executeUpdate();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        model.setId(id.getInt(1));
                    }
                }
            }
            if(Objects.equals(model.getClass(), Candidate.class)) {
                PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate (name) VALUES(?);",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getName());
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    private Model update(Model model) {
        try (Connection cn = pool.getConnection();

        ) {
            if(Objects.equals(model.getClass(), Post.class)) {
                PreparedStatement ps =  cn.prepareStatement("UPDATE post (name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getName());
                ps.executeUpdate();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        model.setId(id.getInt(1));
                    }
                }
            }
            if(Objects.equals(model.getClass(), Candidate.class)) {
                PreparedStatement ps =  cn.prepareStatement("UPDATE candidate (name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getName());
                ps.executeUpdate();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        model.setId(id.getInt(1));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public Model findById(Model model) {
        try (Connection cn = pool.getConnection();

        ) {
             if(Objects.equals(model.getClass(), Post.class)) {
                PreparedStatement ps =  cn.prepareStatement("SELECT FROM post where id = ?)");
                ps.setInt(1, model.getId());
                ps.executeQuery();
                ResultSet rs = ps.getResultSet();
                if (rs.next()) {
                    model = new Post(rs.getInt("id"), rs.getString("name"),
                            rs.getString("description"), rs.getTimestamp("created"));
                }
            }
            if(Objects.equals(model.getClass(), Candidate.class)) {
                PreparedStatement ps =  cn.prepareStatement("SELECT FROM candidates where id = ?)");
                ps.setInt(1, model.getId());
                ps.executeQuery();
                ResultSet rs = ps.getResultSet();
                if (rs.next()) {
                    model = new Candidate(rs.getInt("id"), rs.getString("name"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }



}