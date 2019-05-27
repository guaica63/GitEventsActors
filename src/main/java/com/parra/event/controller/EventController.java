package com.parra.event.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.parra.event.dao.ActorDao;
import com.parra.event.dao.EventDao;
import com.parra.event.dao.RepoDao;
import com.parra.event.entity.Actor;
import com.parra.event.entity.Event;
import com.parra.event.entity.EventActor;

@RestController
public class EventController {
	private static final Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	EventDao eventDao;
	
	@Autowired
	ActorDao actorDao;
	
	@Autowired
	RepoDao repoDao;

	@RequestMapping(value = "/events", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Event> createEvent(@RequestBody Event event) {
		logger.info("Running creatEventy method");
		if (eventDao.findOne(event.getId()) != null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		} else {
			eventDao.createEvent(event);
			return new ResponseEntity(HttpStatus.CREATED);
		}
		
	}

	@RequestMapping(value = "/events", method = RequestMethod.GET, produces = "application/json") 
	public List<Event> getAllEventDetails() {
		logger.info("Running getAllEventDetails method"); 
		countActorEvent();
		return (List<Event>) eventDao.getAllEvent(); 
	}

	
	@RequestMapping(value = "/erase", method = RequestMethod.DELETE, produces = "application/json")
	public void deleteAllEventDetails() {
		logger.info("Running deleteAllEvent method");
		eventDao.deleteAllEvent();
	}
	
	@RequestMapping(value = "/events/actors", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<Event>> getAllEventByActorId(@RequestParam(value="id") Long id) {
		logger.info("Running getAllEventByActorId method");
		List<Event> listTemporalEvent = eventDao.getAllEventByActorId(id);
		if (listTemporalEvent.size() == 0) {
			return new ResponseEntity<List<Event>>(listTemporalEvent, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<Event>>(listTemporalEvent, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/actors", method = RequestMethod.GET, produces = "application/json")
	// @ResponseBody
	public ResponseEntity<List<Actor>> countActorEvent(){
		logger.info("Running countActorEvent method");
		Map<Actor,Integer> eventForActorsMap = new HashMap<Actor, Integer>();
		List<Actor> actorSortId = actorDao.getAllActorByActor();
		for (Actor actsor : actorSortId) {
			eventForActorsMap.put(actsor, eventDao.countEventForActor(actsor.getId()));
		}
		List<Event> eventSortedByDate = eventDao.getAllEvent();
		Comparator<Event> timeComparator = (t1, t2)->t1.getCreated_at().compareTo(t2.getCreated_at());
		eventSortedByDate.sort(timeComparator.reversed());
		
		// eventForActorsMap.forEach((k,v)->System.out.println("Item : " + k + " Count : " + v));
		
		List<EventActor> eventActorList = new ArrayList<EventActor>();
		for (Event eve : eventSortedByDate) {
			EventActor eventActor = new EventActor();
			eventActor.setId(eve.getActor().getId());
		    eventActor.setEvent(eve);
		    eventActor.setQuantity(eventForActorsMap.get(eve.getActor()));
		    if (!eventActorList.contains(eventActor)) {
		    	eventActorList.add(eventActor);
		    } 
		}
		Comparator<EventActor> qComparator = (o1, o2) -> o1.getQuantity().compareTo(o2.getQuantity());
		qComparator.thenComparing((o1, o2) -> o1.getEvent().getCreated_at().compareTo(o2.getEvent().getCreated_at()));
		eventActorList.sort(qComparator.reversed());
		// eventActorList.forEach((ac)->System.out.println(ac));
		boolean howWillBeOrder = false;
		Date comparaCreate = null;
		for (EventActor ea : eventActorList) {
			if (comparaCreate == null || 0 != comparaCreate.compareTo(ea.getEvent().getCreated_at())) {
				comparaCreate = ea.getEvent().getCreated_at();
			} else {
				howWillBeOrder = true;
				break;
			}
		}
		if (howWillBeOrder) {
			Comparator<EventActor> loginComparator = (o1, o2) -> o1.getEvent().getActor().getLogin().compareTo(o2.getEvent().getActor().getLogin());
			eventActorList.sort(loginComparator);
		}
		actorSortId = new ArrayList<Actor>();
		for (EventActor ea : eventActorList) {
			actorSortId.add(ea.getEvent().getActor());
		}
		return new ResponseEntity<List<Actor>>(actorSortId, HttpStatus.OK);

	}
	
	@RequestMapping(value = "/actors/streak", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<Actor>> countActorStreak(){
		logger.info("Running countActorStreak method");
		Map<Actor,Integer> eventForActorsMap = new HashMap<Actor, Integer>();
		List<Actor> actorSortId = actorDao.getAllActorByActor();
		for (Actor actsor : actorSortId) {
			eventForActorsMap.put(actsor, eventDao.countEventForActor(actsor.getId()));
		}
		List<Event> eventSortedByDate = eventDao.getAllEvent();
		Comparator<Event> eventComparator = (e1, e2)->e1.getActor().getId().compareTo(e2.getActor().getId());
		eventComparator.thenComparing((e1, e2) -> e1.getCreated_at().compareTo(e2.getCreated_at()));
		eventSortedByDate.sort(eventComparator);
		
		Map<Actor,Integer> eventForActorsStreak = new HashMap<Actor,Integer>();
		Date compareDate = eventSortedByDate.get(0).getCreated_at();
		Actor compareActor = eventSortedByDate.get(0).getActor();
		int countStreak = 0;
		for (Event eve : eventSortedByDate) {
			if (compareActor == null || compareActor.equals(eve.getActor())) {
				if (compareDate == null || 1 == eve.getCreated_at().compareTo(compareDate)) {
					countStreak++;
				} else {
					compareDate = eve.getCreated_at();
					countStreak = 0;
				}
			} else {
				eventForActorsStreak.put(compareActor, countStreak);
				countStreak = 0;
				compareActor = eve.getActor();
				compareDate = eve.getCreated_at();
			}
		}
		if(eventSortedByDate.size() > 0) {
			eventForActorsStreak.put(compareActor, countStreak);
		}
		actorSortId = createFinalList(eventForActorsMap, eventSortedByDate);
		return new ResponseEntity<List<Actor>>(actorSortId, HttpStatus.OK);
	}
	
	public List<Actor> createFinalList(Map<Actor,Integer> eventForActorsMap, List<Event> eventSortedByDate){
		List<EventActor> eventActorList = new ArrayList<EventActor>();
		for (Event eve : eventSortedByDate) {
			EventActor eventActor = new EventActor();
			eventActor.setId(eve.getActor().getId());
		    eventActor.setEvent(eve);
		    eventActor.setQuantity(eventForActorsMap.get(eve.getActor()));
		    if (!eventActorList.contains(eventActor)) {
		    	eventActorList.add(eventActor);
		    } 
		}
		Comparator<EventActor> qComparator = (o1, o2) -> o1.getQuantity().compareTo(o2.getQuantity());
		qComparator.thenComparing((o1, o2) -> o1.getEvent().getCreated_at().compareTo(o2.getEvent().getCreated_at()));
		eventActorList.sort(qComparator.reversed());
		boolean howWillBeOrder = false;
		Date comparaCreate = null;
		for (EventActor ea : eventActorList) {
			if (comparaCreate == null || 0 != comparaCreate.compareTo(ea.getEvent().getCreated_at())) {
				comparaCreate = ea.getEvent().getCreated_at();
			} else {
				howWillBeOrder = true;
				break;
			}
		}
		if (howWillBeOrder) {
			Comparator<EventActor> loginComparator = (o1, o2) -> o1.getEvent().getActor().getLogin().compareTo(o2.getEvent().getActor().getLogin());
			eventActorList.sort(loginComparator);
		}
		List<Actor> actorSortId = new ArrayList<Actor>();
		for (EventActor ea : eventActorList) {
			actorSortId.add(ea.getEvent().getActor());
		}		
		return actorSortId;
	}
	
}
