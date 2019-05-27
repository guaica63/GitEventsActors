package com.parra.event.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.parra.event.dao.RepoDao;
import com.parra.event.entity.Repo;

@RestController
public class RepoController {
	private static final Logger logger = LoggerFactory.getLogger(RepoController.class);

	@Autowired
	RepoDao repoDao;

	@RequestMapping(value = "/repos", method = RequestMethod.POST, consumes = "application/json")
	public void createRepo(@RequestBody Repo rep) {
		logger.info("Inside the method creatRepoy Repo is " + rep);
		repoDao.createRepo(rep);
	}
	
}
