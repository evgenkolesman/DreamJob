package dream;

import dream.model.Post;
import dream.store.PsqlStorePost;
import dream.store.Store;

import java.sql.Timestamp;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStorePost.instOf();
        store.save(new Post(1, "Java Job", "Good job",
                new Timestamp(System.currentTimeMillis())));
//        for (Model model : store.findAll("Post")) {
//            System.out.println(model.getId() + " " + model.getName());
//        }
        System.out.println(store.findById(1).toString());
    }
}
