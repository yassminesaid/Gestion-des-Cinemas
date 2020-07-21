package org.sid.repositories;

import javax.transaction.Transactional;

import org.sid.dao.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CinemaRepository extends JpaRepository<Cinema, Long> {

	public Page<Cinema> findByNameContains(String mc, Pageable pageable);
	
	@Query("select c from Cinema c where c.ville.id = :id and c.name like :key")
	public Page<Cinema> findByVille(
			@Param("id") Long ville_id, 
			@Param("key") String key,
			Pageable pageable);
	
	@Transactional
	@Modifying
	@Query("delete from Cinema c where c.ville.id = :ville_id")
	public void deleteCinemasByVille(@Param("ville_id") Long ville_id);
	
}
