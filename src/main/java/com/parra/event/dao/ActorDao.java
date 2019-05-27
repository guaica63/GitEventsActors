package com.parra.event.dao;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parra.event.entity.Actor;
import com.parra.event.entity.Event;
import com.parra.event.repository.ActorRepository;

@Service
public class ActorDao {
	
	@Autowired
	ActorRepository actorRepository;
	
	public Actor createActor(Actor act) {
		return actorRepository.save(act);
	}
	
	public List<Actor> getAllActors(){
		return (List<Actor>) actorRepository.findAll();
	}
	
	public Actor findOne(Long id) {
		return actorRepository.findOne(id);
	}
	
	public List<Actor> getAllActorByActor(){
		return ((Collection<Actor>) actorRepository.findAll()).stream()
	            .sorted(Comparator.comparing(Actor::getId))  
	            .collect(Collectors.toList());
	}
	
}
