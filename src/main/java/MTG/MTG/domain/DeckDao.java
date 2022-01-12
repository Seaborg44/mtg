package MTG.MTG.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeckDao extends JpaRepository<Deck, Integer> {

    @Query
    List<Long> fetchDecksIdsByUserId(Long userId);
}
