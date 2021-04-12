/*
 *
 *  Copyright (C) 2021 Piotr Przybył
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package dev.softwaregarden.recordsLibDemo;

import java.io.IOException;

import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.softwaregarden.recordsLibDemo.entities.MEntityBean;
import dev.softwaregarden.recordsLibDemo.entities.MEntityRecord;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public class MorphiaTest {

    @Container
    private final static MongoDBContainer mongoContainer = new MongoDBContainer("mongo:4.4.5");

    @Test
    void checkMorphiaHandlesRecords() throws IOException, InterruptedException {
        var datastore = Morphia.createDatastore(MongoClients.create(mongoContainer.getReplicaSetUrl()), "test");
        datastore.getMapper().map(MEntityBean.class, MEntityRecord.class);

        datastore.save(new MEntityBean(14L));
        datastore.save(new MEntityRecord(15L));

        Assertions.assertEquals(14L, datastore.find(MEntityBean.class).first().getId());
        Assertions.assertEquals(15L, datastore.find(MEntityRecord.class).first().id());

        Assertions.assertEquals(14L, fetchForClass(datastore, MEntityBean.class).getLong("_id"));
        Assertions.assertEquals(15L, fetchForClass(datastore, MEntityRecord.class).getLong("_id"));

    }

    private Document fetchForClass(Datastore datastore, Class<?> klass) throws IOException, InterruptedException {
        var collectionName= datastore.getMapper().getCollection(klass).getNamespace().getCollectionName();
        var execResult = mongoContainer.execInContainer("mongo", "test", "--quiet", "--eval", "db."+collectionName+".find()");
        return Document.parse(execResult.getStdout());
    }
}
