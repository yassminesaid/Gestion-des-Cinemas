package org.sid;

import javax.transaction.Transactional;

import org.sid.dao.Cinema;
import org.sid.dao.Salle;
import org.sid.dao.Ville;
import org.sid.repositories.CinemaRepository;
import org.sid.repositories.PlaceRepository;
import org.sid.repositories.ProjectionRepository;
import org.sid.repositories.SalleRepository;
import org.sid.repositories.TicketRepository;
import org.sid.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeleteController {

	@Autowired private VilleRepository villeRepository;
	@Autowired private CinemaRepository cinemaRepository;
	@Autowired private SalleRepository salleRepository;
	@Autowired private PlaceRepository placeRepository;
	@Autowired private ProjectionRepository projectionRepository;
	@Autowired private TicketRepository ticketRepository;
	
	public void deleteVille(Long id) {
		Ville ville = villeRepository.findById(id).get();
		if(ville == null) return;
		ville.getCinemas().forEach(cinema -> {
			deleteCinema(cinema.getId());
		});
		villeRepository.deleteById(id);
	}
	
	public void deleteCinema(Long id) {
		Cinema cinema = cinemaRepository.findById(id).get();
		if(cinema == null) return;
		cinema.getSalles().forEach(salle -> {
			deleteSalle(salle.getId());
		});
		cinemaRepository.deleteById(id);
	}
	
	public void deleteSalle(Long id) {
		Salle salle = salleRepository.findById(id).get();
		if(salle == null) return;
		placeRepository.deletePlacesBySalle(salle.getId());
		salle.getProjections().forEach(p -> {
			ticketRepository.deleteTicketsByProjection(p.getId());
		});
		projectionRepository.deleteProjectionsBySalle(salle.getId());
		salleRepository.deleteById(id);
	}
	
}
