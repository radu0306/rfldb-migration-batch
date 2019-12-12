package ro.raft.migrationBatch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import ro.astl.services.rfldbapi.league.dto.LeagueNodeIn;

@Component
public class LeagueNodeMapper implements FieldSetMapper<LeagueNodeIn>{

	@Override
	public LeagueNodeIn mapFieldSet(FieldSet fieldSet) throws BindException {
		final LeagueNodeIn leagueNodeIn = new LeagueNodeIn();
		
		leagueNodeIn.setCountryShortName(fieldSet.readString("CountryShortName"));
		leagueNodeIn.setIsRegional(Boolean.valueOf(fieldSet.readString("IsRegional")));
		
		return leagueNodeIn;
	}

}