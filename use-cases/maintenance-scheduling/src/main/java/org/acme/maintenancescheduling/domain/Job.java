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
package org.acme.maintenancescheduling.domain;

import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import org.acme.maintenancescheduling.solver.EndDateUpdatingVariableListener;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.ShadowVariable;

@PlanningEntity
@Entity
public class Job {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int durationInDays;
    private LocalDate readyDate; // Inclusive
    private LocalDate dueDate; // Exclusive
    private LocalDate idealEndDate; // Exclusive

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> tagSet;

    @PlanningVariable
    @ManyToOne
    private Crew crew;
    // Follows the TimeGrain Design Pattern
    @PlanningVariable
    private LocalDate startDate; // Inclusive
    @ShadowVariable(variableListenerClass = EndDateUpdatingVariableListener.class, sourceVariableName = "startDate")
    private LocalDate endDate; // Exclusive

    // No-arg constructor required for Hibernate and OptaPlanner
    public Job() {
    }

    public Job(String name, int durationInDays, LocalDate readyDate, LocalDate dueDate, LocalDate idealEndDate, Set<String> tagSet) {
        this.name = name;
        this.durationInDays = durationInDays;
        this.readyDate = readyDate;
        this.dueDate = dueDate;
        this.idealEndDate = idealEndDate;
        this.tagSet = tagSet;
    }

    public Job(Long id, String name, int durationInDays, LocalDate readyDate, LocalDate dueDate, LocalDate idealEndDate, Set<String> tagSet,
            Crew crew, LocalDate startDate) {
        this.id = id;
        this.name = name;
        this.durationInDays = durationInDays;
        this.readyDate = readyDate;
        this.dueDate = dueDate;
        this.idealEndDate = idealEndDate;
        this.tagSet = tagSet;
        this.crew = crew;
        this.startDate = startDate;
        this.endDate = EndDateUpdatingVariableListener.calculateEndDate(startDate, durationInDays);
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    @PlanningId
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public LocalDate getReadyDate() {
        return readyDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getIdealEndDate() {
        return idealEndDate;
    }

    public Set<String> getTagSet() {
        return tagSet;
    }

    public Crew getCrew() {
        return crew;
    }

    public void setCrew(Crew crew) {
        this.crew = crew;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
