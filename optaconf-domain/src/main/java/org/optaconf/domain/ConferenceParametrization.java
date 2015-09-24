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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.optaplanner.core.api.domain.solution.cloner.DeepPlanningClone;

@DeepPlanningClone
@Entity(name = "optaconf_conference_parametrization")
public class ConferenceParametrization extends AbstractPersistable {

    public static ConferenceParametrization createDefault(Conference conference) {
        ConferenceParametrization conferenceParametrization = new ConferenceParametrization();
        conferenceParametrization.setConference(conference);
        conferenceParametrization.setTrackConflictWeight(-100);
        return conferenceParametrization;
    }

    @NotNull
    @OneToOne()
    @JoinColumn(name = "conference_id")
    @JsonBackReference
    private Conference conference;

    @NotNull @Max(0)
    private int trackConflictWeight;

    public ConferenceParametrization() {
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public int getTrackConflictWeight() {
        return trackConflictWeight;
    }

    public void setTrackConflictWeight(int trackConflictWeight) {
        this.trackConflictWeight = trackConflictWeight;
    }

}
