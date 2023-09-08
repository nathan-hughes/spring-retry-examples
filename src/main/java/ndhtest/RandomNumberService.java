package ndhtest;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class RandomNumberService {

    private Random random = new Random();

    public int randomNumber() {
        return random.nextInt();
    }
}
