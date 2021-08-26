package dream.store;

import dream.model.Post;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Junior vacancy ", new Timestamp(System.currentTimeMillis())));
        posts.put(2, new Post(2, "Middle Java Job","Middle vacancy ", new Timestamp(System.currentTimeMillis())));
        posts.put(3, new Post(3, "Senior Java Job", "Senior vacancy ", new Timestamp(System.currentTimeMillis())));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }
}
