package ro.raft.migrationBatch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import ro.astl.services.rfldbapi.league.dto.LeagueIn;

@Component
public class LeagueMapper implements FieldSetMapper<LeagueIn>{

	@Override
	public LeagueIn mapFieldSet(FieldSet fieldSet) throws BindException {
		final LeagueIn leagueIn = new LeagueIn();
		
		leagueIn.setLeagueName(fieldSet.readString("LeagueName"));
		leagueIn.setLeagueNodeName(fieldSet.readString("LeagueNode"));
		
		return leagueIn;
	}

}