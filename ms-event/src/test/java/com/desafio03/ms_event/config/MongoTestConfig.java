package com.desafio03.ms_event.config;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MongoTestConfig {
    private static MongodExecutable mongodExecutable;
    private static MongodProcess mongodProcess;

    @BeforeAll
    public static void startMongo() throws Exception {
        MongodConfig mongodConfig = MongodConfig.builder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", 27017, false))
                .build();

        mongodExecutable = MongodStarter.getDefaultInstance().prepare(mongodConfig);
        mongodProcess = mongodExecutable.start();
    }

    @AfterAll
    public static void stopMongo() {
        if (mongodProcess != null) {
            mongodProcess.stop();
        }
    }

    /*
    @Test
    public void testDatabase() {
        assertNotNull(mongodProcess);
    }
     */
}
