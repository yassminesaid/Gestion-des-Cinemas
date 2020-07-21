package org.sid.repositories;

import org.sid.dao.Categorie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Categorie, Long> {

	public Page<Categorie> findByNameContains(String mc, Pageable pageable);
	
}
