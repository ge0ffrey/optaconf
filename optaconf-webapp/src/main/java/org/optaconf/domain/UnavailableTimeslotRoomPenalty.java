/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaconf.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

import com.fasterxml.jackson.annotation.JsonBackReference;

@DeepPlanningClone
@Entity(name = "optaconf_unavailtimeslotroompenalty")
public class UnavailableTimeslotRoomPenalty extends AbstractConferencedPersistable {

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Timeslot timeslot;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Room room;

    public UnavailableTimeslotRoomPenalty() {
    }

    public UnavailableTimeslotRoomPenalty(Conference conference, String externalId, Timeslot timeslot, Room room) {
        super(conference, externalId);
        this.timeslot = timeslot;
        this.room = room;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

}
