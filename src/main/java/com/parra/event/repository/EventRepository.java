package com.parra.event.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.parra.event.entity.Event;

public interface EventRepository extends CrudRepository<Event,Long> {

}
