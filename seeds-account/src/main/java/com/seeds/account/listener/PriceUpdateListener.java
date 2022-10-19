package com.seeds.account.listener;


import com.seeds.account.enums.PriceUpdateEvent;

/**
 *
 * @author milo
 *
 */
public interface PriceUpdateListener {

    void onEvent(PriceUpdateEvent event);

}
