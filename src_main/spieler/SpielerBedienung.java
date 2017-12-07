package spieler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpielerBedienung {


    @Autowired
    SpielerRepository spielerRepository;

    public Spieler addSpieler(Spieler spieler) {
        return spielerRepository.save(spieler);
    }

    public List<Spieler> getAllSpielers() {
        List<Spieler> spielers = new ArrayList<>();
        spielerRepository.findAll().forEach(spielers::add);
        return spielers;
    }

    public Spieler getOne(Integer id) {
       return spielerRepository.findOne(id);
    }
}
