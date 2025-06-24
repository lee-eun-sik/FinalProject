package back.mapper.home;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import back.model.home.Home;

@Mapper
public interface HomeMapper {
	
	public List<Home> getAnimalList (Home home);
	
	public List<Home> getPlantList (Home home);

}
