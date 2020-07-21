package org.sid.repositories;


import javax.transaction.Transactional;

import org.sid.dao.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Transactional
	@Modifying
	@Query("delete from Ticket t where t.projection.id = :projection_id")
	public void deleteTicketsByProjection(@Param("projection_id") Long projection_id);
	
	@Query("select t from Ticket t where t.projection.id = :id")
	public Page<Ticket> findByProjection(
			@Param("id") Long projection_id, 
			Pageable pageable);
	
}
