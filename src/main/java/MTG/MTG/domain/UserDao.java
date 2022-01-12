package MTG.MTG.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<Nutzer, Long> {

    Nutzer getByUsername(String username);
}
