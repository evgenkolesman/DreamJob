package dream.store;

import dream.model.Candidate;
import dream.model.Model;
import dream.model.Post;


import java.sql.Timestamp;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class MemStore implements Store {
    private static AtomicInteger POST_ID = new AtomicInteger(4);
    private static AtomicInteger CANDIDATE_ID = new AtomicInteger(5);

    private static final MemStore INST = new MemStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
        private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "Junior vacancy ", new Timestamp(System.currentTimeMillis())));
        posts.put(2, new Post(2, "Middle Java Job","Middle vacancy ", new Timestamp(System.currentTimeMillis())));
        posts.put(3, new Post(3, "Senior Java Job", "Senior vacancy ", new Timestamp(System.currentTimeMillis())));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static MemStore instOf() {
        return INST;
    }

    public void save(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public Candidate findByIdCandidates(int id) {
        return candidates.get(id);
    }

    public void save(Candidate candidate) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
            candidates.put(candidate.getId(), candidate);
        }

    @Override
    public Collection findAll() {
        return null;
    }

    @Override
    public void save(Model model) {

    }


}

