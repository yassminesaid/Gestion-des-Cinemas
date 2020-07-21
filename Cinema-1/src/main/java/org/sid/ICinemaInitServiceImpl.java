package org.sid;

import java.time.LocalDate;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.sid.dao.Categorie;
import org.sid.dao.Cinema;
import org.sid.dao.Film;
import org.sid.dao.Place;
import org.sid.dao.Projection;
import org.sid.dao.Salle;
import org.sid.dao.Seance;
import org.sid.dao.Ticket;
import org.sid.dao.Ville;
import org.sid.repositories.CategoryRepository;
import org.sid.repositories.CinemaRepository;
import org.sid.repositories.FilmRepository;
import org.sid.repositories.PlaceRepository;
import org.sid.repositories.ProjectionRepository;
import org.sid.repositories.SalleRepository;
import org.sid.repositories.SeanceRepository;
import org.sid.repositories.TicketRepository;
import org.sid.repositories.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ICinemaInitServiceImpl implements ICinemaInitService{

	@Autowired private VilleRepository villeRepository;
	@Autowired private CinemaRepository cinemaRepository;
	@Autowired private SalleRepository salleRepository;
	@Autowired private PlaceRepository placeRepository;
	@Autowired private SeanceRepository	seanceRepository;
	@Autowired private FilmRepository filmRepository;
	@Autowired private ProjectionRepository projectionRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private TicketRepository ticketRepository;
	
	@Override
	public void initVilles() {
		Stream.of("Casablanca", "Marrakech", "Rabat", "Tanger").forEach(nameVille -> {
			Ville ville = new Ville();
			ville.setName(nameVille);
			villeRepository.save(ville);
		});
	}

	@Override
	public void initCinemas() {
		villeRepository.findAll().forEach(v -> {
			Stream.of("MegaRama", "IMAX", "FOUNOUN", "CHAHRAZAD", "DAOULIZ")
			.forEach(c -> {
				Cinema cinema = new Cinema();
				cinema.setName(c);
				cinema.setNombreSalles(3+(int)(Math.random()*7));
				cinema.setVille(v);
				cinemaRepository.save(cinema);
			});
		});
	}

	@Override
	public void initSalles() {
		cinemaRepository.findAll().forEach(cinema -> {
			for(int i = 0; i < cinema.getNombreSalles(); i++) {
				Salle salle = new Salle();
				salle.setName("Salle" + (i + 1));
				salle.setCinema(cinema);
				salle.setNombrePlace(15+(int)(Math.random()*20));
				salleRepository.save(salle);
			}
		});
	}

	@Override
	public void initPlaces() {
		salleRepository.findAll().forEach(salle -> {
			for(int i = 0; i < salle.getNombrePlace(); i++) {
				Place place = new Place();
				place.setNumero(i+1);
				place.setSalle(salle);
				placeRepository.save(place);
			}
		});
	}

	@Override
	public void initSeances() {
		//DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Stream.of("12:00:00", "15:00:00", "17:00:00", "19:00:00", "21:00:00")
			.forEach(s -> {
			Seance seance = new Seance();
			seance.setHeureDebut(Time.valueOf(s));
			seanceRepository.save(seance);
			/*
			try {
				//dateFormat.parse(s)
			}catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			*/
		});
	}

	@Override
	public void initCategories() {
		Stream.of("Histoire", "Actions", "Fiction", "Drama").forEach(c -> {
			Categorie categorie = new Categorie();
			categorie.setName(c);
			categoryRepository.save(categorie);
		});
	}

	@Override
	public void initFilms() {
		double durees[] = new double[] {1, 1.5, 2, 2.5, 3};
		List<Categorie> categories = categoryRepository.findAll();
		Stream.of("12 Homme en colere", "Forrest Gump", "Green Book",
				"La ligne Verte", "Le Parrain", "Le Seigneur des anneaux")
		.forEach(f -> {
			Film film = new Film();
			film.setTitre(f);
			film.setDuree(durees[new Random().nextInt(durees.length)]);
			film.setPhoto(f.replaceAll(" ", "")+".jpg");
			film.setCategorie(categories.get(new Random().nextInt(categories.size())));
			filmRepository.save(film);
		});
	}

	@Override
	public void initProjections() {
		double prices[] = new double[] {30, 50, 60, 70, 90, 100};
		List<Film> films = filmRepository.findAll();
		int index = 0;
		for(Ville v : villeRepository.findAll()) {
			for(Cinema c : v.getCinemas()) {
				for(Salle salle : c.getSalles()) {
					for(Seance seance : seanceRepository.findAll()) {
						if(index >= films.size()) index = 0;
						Projection projection = new Projection();
						projection.setDateProjection(Date.valueOf(LocalDate.now()));
						projection.setFilm(films.get(index++));
						projection.setPrix(prices[new Random().nextInt(prices.length)]);
						projection.setSalle(salle);
						projection.setSeance(seance);
						projectionRepository.save(projection);
					}
				}
			}
		}/*
		villeRepository.findAll().forEach(ville -> {
			ville.getCinemas().forEach(cinema -> {
				cinema.getSalles().forEach(salle -> {
					filmRepository.findAll().forEach(film -> {
						seanceRepository.findAll().forEach(seance -> {
							Projection projection = new Projection();
							projection.setDateProjection(new Date());
							projection.setFilm(film);
							projection.setPrix(prices[new Random().nextInt(prices.length)]);
							projection.setSalle(salle);
							projection.setSeance(seance);
							projectionRepository.save(projection);
						});
					});
				});
			});
		});
		*/
	}

	@Override
	public void initTickets() {
		projectionRepository.findAll().forEach(proj -> {
			proj.getSalle().getPlaces().forEach(place -> {
				Ticket ticket = new Ticket();
				ticket.setPlace(place);
				ticket.setPrix(proj.getPrix());
				ticket.setProjection(proj);
				ticket.setReserve(false);
				ticketRepository.save(ticket);
			});
		});
	}

}
