package turn;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TurnBedienung {

    @Autowired
    TurnRepository turnRepository;

    public void addTurn(Turn turn) {
        turnRepository.save(turn);
    }
}