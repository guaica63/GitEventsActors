package com.parra.event.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.parra.event.entity.Repo;
import com.parra.event.repository.RepoRepository;

@Service
public class RepoDao {
	
	@Autowired
	RepoRepository repoRepository;
	
	public Repo createRepo(Repo rep) {
		return repoRepository.save(rep);
	}
}
