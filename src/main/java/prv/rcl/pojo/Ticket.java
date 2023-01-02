package prv.rcl.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ticket {

    private static final Logger log = LoggerFactory.getLogger(Ticket.class);

    public void destory() {
        log.info("ticket bean has been destory");
    }

    public void initTicket() {
        log.info("ticket creating!");
    }
}
