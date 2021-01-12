package auto.spring.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auto.spring.dao.CommandeDAO;
import auto.spring.dao.ProduitDAO;
import auto.spring.exception.PasDeCommandeSuivanteException;
import auto.spring.modele.Commande;

@Service
public class CommandeService {
	
	private ProduitDAO produitDao;
	private CommandeDAO commandeDao;
	
	@Autowired
	public CommandeService(ProduitDAO produitDao, CommandeDAO commandeDao) {
		this.produitDao = produitDao;
		this.commandeDao = commandeDao;
	}

	@Transactional
	public Commande commander(CommandeDto commandeDto) {
		Commande commande = new Commande();
		commande.setDestinataire(commandeDto.getNom());
		commande.setEnAttente(true);
		commande.setDate_commande(new Date());
		for(long id : commandeDto.getProduitId()) {
			commande.ajouter(produitDao.getRecapById(id));
		}
		commandeDao.sauver(commande);
		return commande;
	}

	public Commande getRecap(long idCommande) {
		return commandeDao.getRecapById(idCommande);
	}

	public Commande getCommandeSuivante() throws PasDeCommandeSuivanteException {
		return commandeDao.getCommandeSuivante();
	}

	public List<Commande> getCommandesEnAttente() {
		return commandeDao.getCommandesEnAttente();
	}

	@Transactional
	public void signalerCommandePrete(long id) {
		commandeDao.signalerCommandePrete(id);
	}

}
