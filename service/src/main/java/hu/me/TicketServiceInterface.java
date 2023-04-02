package hu.me;

import hu.me.domain.User;
import org.springframework.stereotype.Service;

@Service
public interface TicketServiceInterface {

    User getLoggedInUser();


}
