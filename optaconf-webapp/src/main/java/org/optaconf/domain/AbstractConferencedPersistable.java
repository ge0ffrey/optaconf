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
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class AbstractConferencedPersistable extends AbstractPersistable {

    // TODO @NaturalId for the combination of conference and externalId

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "conference_id")
    @JsonBackReference
    protected Conference conference;

    @NotNull @Size(max = 240)
    @Column(name = "external_id")
    protected String externalId;

    protected AbstractConferencedPersistable() {}

    public AbstractConferencedPersistable(Conference conference, String externalId) {
        this.conference = conference;
        this.externalId = externalId;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    // ************************************************************************
    // Real methods
    // ************************************************************************

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        AbstractConferencedPersistable other = (AbstractConferencedPersistable) o;
        if (getExternalId() == null) {
            if (other.getExternalId() != null) {
                return false;
            }
        } else if (!getExternalId().equals(other.getExternalId())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getExternalId() == null) ? 0 : getExternalId().hashCode());
        return result;
    }

    public String toString() {
        return "[" + getClass().getSimpleName() + "-" + id + (externalId == null ? "" : " (" + externalId + ")") + "]";
    }

}
