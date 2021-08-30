package dream;

import dream.model.Post;
import dream.store.PsqlStore;
import dream.store.Store;

import java.sql.Timestamp;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job", "Good job", new Timestamp(System.currentTimeMillis())));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
    }
}
