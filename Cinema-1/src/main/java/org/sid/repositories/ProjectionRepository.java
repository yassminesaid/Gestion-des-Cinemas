package org.sid.repositories;

import java.sql.Date;
import java.util.Collection;

import javax.transaction.Transactional;

import org.sid.dao.Projection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProjectionRepository extends JpaRepository<Projection, Long> {
	
	public Page<Projection> findByDateProjection(Date date, Pageable pageable);
	
	@Query("select p from Projection p where p.salle.id = :id and p.dateProjection = :date")
	public Page<Projection> findByDateAndSalle(
			@Param("id") Long salle_id, 
			@Param("date") Date date,
			Pageable pageable);
	
	@Query("select p from Projection p where p.salle.id = :salle_id and"
			+ " p.dateProjection = :date and p.seance.id = :seance_id")
	public Projection findByDateAndSalleAndSeance(
			@Param("salle_id") Long salle_id, 
			@Param("date") Date date,
			@Param("seance_id") Long seance_id);
	
	
	@Query("select p from Projection p where p.salle.id = :salle_id and"
			+ " p.dateProjection = :date")
	public Collection<Projection> findProjectionBySalleAndDate(
			@Param("salle_id") Long salle_id, 
			@Param("date") Date date);
	
	@Transactional
	@Modifying
	@Query("delete from Projection p where p.salle.id = :salle_id")
	public void deleteProjectionsBySalle(@Param("salle_id") Long salle_id);
	
}
