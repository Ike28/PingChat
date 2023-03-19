package com.map222.sfmc.socialnetworkfx.userinterface;

import com.map222.sfmc.socialnetworkfx.domain.business.Event;
import com.map222.sfmc.socialnetworkfx.service.MasterService;

import java.util.List;

public class Test {
    public static void main(String[] args) {

        MasterService service = new MasterService();
        List<Event> events = service.getAllEvents();
    }
}
