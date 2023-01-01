package prv.rcl.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prv.rcl.service.UserService;

/**
 * @author rcl
 */
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void execute() {
        log.info("execute method :{}",message);
    }
}
