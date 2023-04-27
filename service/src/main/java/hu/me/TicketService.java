package hu.me;

import hu.me.domain.User;
import hu.me.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService implements TicketServiceInterface {

    private User loggedInUser;
    @Autowired
    private UserRepository uRep;

    private List<User> users;

    public TicketService() {}

    @Override
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public User findUserByUsername(String username) {

        users = uRep.findAll();

        return users.stream()
                .filter(user -> user.getCredentials().getLoginName().equals(username))
                .findFirst().orElse(null);

    }
    public void save(User user) {
        uRep.save(user);
    }

}
