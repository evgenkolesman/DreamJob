package dream.store;

import dream.model.Model;

import java.util.Collection;

public interface Store {
    Collection<Model> findAll(String className);

    void save(Model model);

    Model findById(Model model);

}
