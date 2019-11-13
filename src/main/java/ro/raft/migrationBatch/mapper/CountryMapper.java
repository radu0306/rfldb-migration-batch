package ro.raft.migrationBatch.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import ro.astl.services.rfldbapi.country.dto.CountryIn;

@Component
public class CountryMapper implements FieldSetMapper<CountryIn>{

	@Override
	public CountryIn mapFieldSet(FieldSet fieldSet) throws BindException {
		final CountryIn countryIn = new CountryIn();
		
		countryIn.setCountryName(fieldSet.readString("CountryName"));
		countryIn.setCountryShortName(fieldSet.readString("CountryShortName"));
		
		return countryIn;
	}

}
