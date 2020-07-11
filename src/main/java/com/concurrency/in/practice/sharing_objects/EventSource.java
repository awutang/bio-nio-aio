/**
 * Copyright 2014-2016 www.fangdd.com All Rights Reserved.
 * Author: Tang Yuqian <tangyuqian-sz@fangdd.com>
 * Date: 2020/6/21
 */
package com.concurrency.in.practice.sharing_objects;

import java.util.EventListener;

interface EventSource {
    public void registerListener(EventListener eventListener);
}

class EventSourceImpl implements EventSource {

    // @Override
    public void registerListener(EventListener eventListener) {

    }
}

//interface EventListener {
//    void onEvent(Event e);
//}
//
//interface Event {
//}
