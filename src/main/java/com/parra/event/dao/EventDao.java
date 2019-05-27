package com.parra.event.dao;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parra.event.entity.Event;
import com.parra.event.repository.EventRepository;

@Service
public class EventDao {
	
	@Autowired
	EventRepository eventRepository;
	
	public Event createEvent(Event evt) {
		return eventRepository.save(evt);
	}
	
	public List<Event> getAllEvent(){
		return ((Collection<Event>) eventRepository.findAll()).stream()
	            .sorted(Comparator.comparing(Event::getId))   // sort by Event Id
	            .collect(Collectors.toList());
	}
	
	public void deleteAllEvent(){
		eventRepository.delete(getAllEvent());
	}
	
	public List<Event> getAllEventByActorId(Long actorId){
		return getAllEvent().stream()
				            .filter(e -> e.getActor().getId() == actorId) // only actorId
				            .collect(Collectors.toList());
	}
	
	public Event findOne(Long id) {
		return eventRepository.findOne(id);
	}
	
	public Integer countEventForActor(Long id) {
		return getAllEventByActorId(id).size();
	}
}
