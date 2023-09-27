/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.acme.schooltimetabling.rest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.acme.schooltimetabling.domain.Room;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class RoomResourceTest {

    @Test
    public void getAll() {
        List<Room> roomList = given()
                .when().get("/rooms")
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList(".", Room.class);
        assertFalse(roomList.isEmpty());
        Room firstRoom = roomList.get(0);
        assertEquals("Room A", firstRoom.getName());
    }

    @Test
    void addAndRemove() {
        Room room = given()
                .when()
                .contentType(ContentType.JSON)
                .body(new Room("Test room"))
                .post("/rooms")
                .then()
                .statusCode(201)
                .extract().as(Room.class);

        given()
                .when()
                .delete("/rooms/{id}", room.getId())
                .then()
                .statusCode(204);
    }

}
