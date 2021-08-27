package dream.store;

import dream.model.Candidate;
import dream.model.Post;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Store {

    private static final Store INST = new Store();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private Store() {
        posts.put(1, new Post(1, "Junior Java Job", "Junior vacancy ", new Timestamp(System.currentTimeMillis())));
        posts.put(2, new Post(2, "Middle Java Job","Middle vacancy ", new Timestamp(System.currentTimeMillis())));
        posts.put(3, new Post(3, "Senior Java Job", "Senior vacancy ", new Timestamp(System.currentTimeMillis())));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static Store instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }
}
