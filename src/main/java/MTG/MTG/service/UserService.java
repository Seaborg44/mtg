package MTG.MTG.service;

import MTG.MTG.domain.Nutzer;
import MTG.MTG.domain.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    public void saveUser(Nutzer nutzer) {
        userDao.save(nutzer);
    }
    public Optional<Nutzer> findUserById(Long id) {
        return userDao.findById(id);
    }
}
