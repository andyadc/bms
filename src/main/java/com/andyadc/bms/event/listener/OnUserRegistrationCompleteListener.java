package com.andyadc.bms.event.listener;

import com.andyadc.bms.auth.entity.AuthUser;
import com.andyadc.bms.event.OnUserRegistrationCompleteEvent;
import org.apache.commons.lang3.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Order(1)
@Component
public class OnUserRegistrationCompleteListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(OnUserRegistrationCompleteListener.class);

    //TODO
    @Async
    @Override
    public void onApplicationEvent(OnUserRegistrationCompleteEvent event) {
        try {
            AuthUser authUser = event.getAuthUser();
            logger.info("AuthUser {} register completed", authUser.getUsername());
            ThreadUtils.sleep(Duration.ofSeconds(3L));
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
