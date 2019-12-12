package ro.raft.migrationBatch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import ro.astl.services.rfldbapi.club.dto.ClubIn;

@Component
public class ClubMapper implements FieldSetMapper<ClubIn>{

	@Override
	public ClubIn mapFieldSet(FieldSet fieldSet) throws BindException {
		final ClubIn clubIn = new ClubIn();
		
		clubIn.setClubName(fieldSet.readString("ClubName"));
		clubIn.setClubShortName(fieldSet.readString("ClubNameShort"));
		clubIn.setLeagueName(fieldSet.readString("LeagueName"));
		clubIn.setClubFMId(fieldSet.readString("ClubFMId"));
		
		return clubIn;
	}

}
