package dream.store;

import dream.model.Model;

import java.util.Collection;

public interface Store {
    Collection findAll();

    void save(Model model);

    Model findById(int id);
}
