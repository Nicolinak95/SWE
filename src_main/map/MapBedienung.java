package map;


import spiel.SpielRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapBedienung {

    @Autowired
    MapRepository mapRepository;

    public void save(Map spielMap) {
        mapRepository.save(spielMap);
    }
}
