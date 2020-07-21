package org.sid.repositories;

import javax.transaction.Transactional;

import org.sid.dao.Salle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SalleRepository extends JpaRepository<Salle, Long> {

	@Query("select s from Salle s where s.cinema.id = :id and s.name like :key")
	public Page<Salle> findByCinema(
			@Param("id") Long cinema_id, 
			@Param("key") String key,
			Pageable pageable);
	
	@Transactional
	@Modifying
	@Query("delete from Salle s where s.cinema.id = :cinema_id")
	public void deleteSallesByCinema(@Param("cinema_id") Long cinema_id);
	
}
