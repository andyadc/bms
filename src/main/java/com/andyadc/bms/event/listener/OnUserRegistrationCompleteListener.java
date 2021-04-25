package com.andyadc.bms.event.listener;

import com.andyadc.bms.auth.entity.AuthUser;
import com.andyadc.bms.event.OnUserRegistrationCompleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OnUserRegistrationCompleteListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(OnUserRegistrationCompleteListener.class);

    //TODO
    @Async
    @Override
    public void onApplicationEvent(OnUserRegistrationCompleteEvent event) {
        AuthUser authUser = event.getAuthUser();
        logger.info("AuthUser {} register completed", authUser.getUsername());
    }
}
