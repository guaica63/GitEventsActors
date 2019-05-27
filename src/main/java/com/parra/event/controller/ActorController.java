package com.parra.event.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.parra.event.dao.ActorDao;
import com.parra.event.entity.Actor;

@RestController
public class ActorController {
	private static final Logger logger = LoggerFactory.getLogger(ActorController.class);

	@Autowired
	ActorDao actorDao;

	@RequestMapping(value = "/actors", method = RequestMethod.POST, consumes = "application/json")
	public void createActor(@RequestBody Actor act) {
		logger.info("Inside the method creatActory Actor is " + act);
		actorDao.createActor(act);
	}
	
	@RequestMapping(value = "/actors", method = RequestMethod.PUT,consumes = "application/json")
	@ResponseBody
	public ResponseEntity<Object> updateActor(@RequestBody Actor act) {
		logger.info("Running Actor avatar update"  + act);
		if (actorDao.findOne(act.getId()) == null) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		} else { 
			Actor temporalActor = actorDao.findOne(act.getId());
			if(!temporalActor.getLogin().equals(act.getLogin())) {
				actorDao.createActor(act);
				return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
			} else {
				if(!temporalActor.getAvatar_url().equals(act.getAvatar_url())) {
					actorDao.createActor(act);
					return new ResponseEntity<Object>(HttpStatus.OK);					
				} else {
					return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
				}
			}
		}

	}
	
	@RequestMapping(value = "/allActors", method = RequestMethod.GET)
	public List<Actor> getAllActor() {
		logger.info("Inside the method getAllActors ");
		return actorDao.getAllActors();
	}
	
	
}
