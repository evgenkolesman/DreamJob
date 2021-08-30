package dream;

import dream.model.Model;
import dream.model.Post;
import dream.store.PsqlStore;
import dream.store.Store;

import java.sql.Timestamp;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.save(new Post(0, "Java Job", "Good job", new Timestamp(System.currentTimeMillis())));
        for (Model model : store.findAll("Post")) {
            System.out.println(model.getId() + " " + model.getName());
        }
    }
}
