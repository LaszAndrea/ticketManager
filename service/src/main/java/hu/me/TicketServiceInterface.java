package hu.me;

import org.springframework.stereotype.Service;

@Service
public interface TicketServiceInterface {

    User getLoggedInUser();


}
