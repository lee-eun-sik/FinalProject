package back.service.home;

import java.io.IOException;
import java.util.List;
import back.model.home.Home;

public interface HomeService {
	
	public List<Home>getAnimalList(Home home);
	
	public List<Home>getPlantList(Home home);
	
}