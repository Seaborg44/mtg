package MTG.MTG.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardDao extends CrudRepository<Card, Long> {
}
