package dream.store;

import dream.model.Candidate;
import dream.model.Model;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class PsqlStoreCandidate implements Store{

    private static final Logger logger = Logger.getLogger(PsqlStorePost.class);
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlStoreCandidate() {
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
        private static final Store INST = new PsqlStoreCandidate();
    }

    public static Store instOf() {
        return PsqlStoreCandidate.Lazy.INST;
    }

    @Override
    public Collection<Candidate> findAll() {
        List<Candidate> models = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    models.add(new Candidate(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return models;
    }

    @Override
    public void save(Model model) {
        Candidate candidate = (Candidate) model;
        if (candidate.getId() == 0) {
            create(candidate);
        } else {
            update(candidate);
        }
    }

    private Model create(Candidate candidate) {
        try (Connection cn = pool.getConnection()) {
                PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate (name) VALUES (?);",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, candidate.getName());
                ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return candidate;
    }

    private Model update(Model model) {
        try (Connection cn = pool.getConnection()) {
                PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = ?",
                        PreparedStatement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getName());
                ps.executeUpdate();
                try (ResultSet id = ps.getGeneratedKeys()) {
                    if (id.next()) {
                        model.setId(id.getInt(1));
                    }
                }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return model;
    }

    @Override
    public Model findById(int id) {
        Candidate model = null;
        try (Connection cn = pool.getConnection()) {
                PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate where id=?");
                ps.setInt(1, id);
                ps.executeQuery();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        model = new Candidate(id, rs.getString("name"));
                    }
                }
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return model;
    }
}
