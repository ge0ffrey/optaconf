/*
 * Copyright 2010 JBoss Inc
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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class AbstractPersistable implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    protected Long id;
    
    @Column(name="external_id")
    protected String externalId;
    
    @Version
    protected Long version;

    protected AbstractPersistable() {
    }

    protected AbstractPersistable(String  externalId) {
        this.externalId = externalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getExternalId() {
      return externalId;
    }

    public void setExternalId(String externalId) {
      this.externalId = externalId;
    }

    public Long getVersion() {
      return version;
    }

    protected void setVersion(Long version) {
      this.version = version;
    }

    public String toString() {
        return "[" + getClass().getName().replaceAll(".*\\.", "") + "=> ID: " + id + ", External ID: "+externalId+"]";
    }

}
