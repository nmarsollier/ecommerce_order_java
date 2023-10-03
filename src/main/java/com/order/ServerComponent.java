package com.order;

import dagger.Component;

import javax.inject.Singleton;

@Component()
@Singleton
public interface ServerComponent {
    Server getServer();
}