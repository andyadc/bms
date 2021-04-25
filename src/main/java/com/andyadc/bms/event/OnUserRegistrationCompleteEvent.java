package com.andyadc.bms.event;

import com.andyadc.bms.auth.entity.AuthUser;
import org.springframework.context.ApplicationEvent;

public class OnUserRegistrationCompleteEvent extends ApplicationEvent {

    private AuthUser authUser;

    public OnUserRegistrationCompleteEvent(AuthUser authUser) {
        super(authUser);
        this.authUser = authUser;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }
}
