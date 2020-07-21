package org.sid.repositories;

import javax.transaction.Transactional;

import org.sid.dao.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlaceRepository extends JpaRepository<Place, Long> {
	
	@Transactional
	@Modifying
	@Query("delete from Place p where p.salle.id = :salle_id")
	public void deletePlacesBySalle(@Param("salle_id") Long salle_id);
	
}
