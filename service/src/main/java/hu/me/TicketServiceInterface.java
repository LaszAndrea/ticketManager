package hu.me;

import hu.me.domain.Sights;
import hu.me.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketServiceInterface {

    User getLoggedInUser();
    User findUserByUsername(String username);

    void save(User user);

    List<Sights> getSights();


}
