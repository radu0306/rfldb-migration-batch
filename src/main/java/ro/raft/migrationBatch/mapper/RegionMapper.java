package ro.raft.migrationBatch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import ro.astl.services.rfldbapi.country.dto.RegionIn;
import ro.raft.migrationBatch.constants.MigrationBatchConstants;

public class RegionMapper implements FieldSetMapper<RegionIn>{

	@Override
	public RegionIn mapFieldSet(FieldSet fieldSet) throws BindException {
		final RegionIn regionIn = new RegionIn();
		
		regionIn.setRegionName(fieldSet.readString("RegionName"));
		regionIn.setRegionShortName(fieldSet.readString("RegionShortName"));
		regionIn.setCountryShortName(MigrationBatchConstants.RomaniaCountryShortName);
		
		return regionIn;
	}

}