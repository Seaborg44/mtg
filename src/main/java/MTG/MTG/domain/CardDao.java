package MTG.MTG.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardDao extends JpaRepository<Card, Long> {

    Card findCardById(Long id);

    Long fetchCardByName(String name);


}
