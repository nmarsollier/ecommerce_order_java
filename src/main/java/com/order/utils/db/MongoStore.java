package com.order.utils.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.order.events.schema.Event;
import com.order.projections.orderStatus.schema.OrderStatus;
import com.order.utils.server.Environment;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import javax.inject.Inject;

/**
 * Permite la configuración del acceso a la db
 */
public class MongoStore {

    final Environment environment;


    private final MongoDatabase database;

    @Inject
    public MongoStore(Environment environment) {
        this.environment = environment;

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());

        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(environment.databaseUrl()))
                .codecRegistry(codecRegistry)
                .build();

        MongoClient client = MongoClients.create(clientSettings);

        database = client.getDatabase("orders_kotlin");
    }

    public MongoCollection<Event> getEventCollection() {
        return database.getCollection("event", Event.class);
    }

    public MongoCollection<OrderStatus> getOrderStatusCollection() {
        return database.getCollection("orders_projection", OrderStatus.class);
    }
}