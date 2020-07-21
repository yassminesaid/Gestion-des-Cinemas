package org.sid.repositories;

import org.sid.dao.Ville;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VilleRepository extends JpaRepository<Ville, Long> {
	
	public Page<Ville> findByNameContains(String mc, Pageable pageable);
	
}
