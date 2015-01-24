package de.avci.joride.restful.converters;

import de.avci.joride.restful.dto.customers.CustomerDTO;
import de.fhg.fokus.openride.customerprofile.CustomerEntity;

public class CustomerDTOConverter {

	public CustomerDTO customerDTO(CustomerEntity entity) {
	
		CustomerDTO res=new CustomerDTO();
		
		res.setCustId(entity.getCustId());
		res.setCustNickname(entity.getCustNickname());
		res.setCustAccountBalance(entity.getCustAccountBalance());
		res.setCustAddrCity(entity.getCustAddrCity());
		res.setCustAddrStreet(entity.getCustAddrStreet());
		res.setCustAddrZipcode(entity.getCustAddrZipcode());
		res.setCustBankAccount(entity.getCustBankAccount());
		res.setCustBankCode(entity.getCustBankCode());
		res.setCustDateofbirth(entity.getCustDateofbirth());
		res.setCustDriverprefAge(entity.getCustDriverprefAge());
		res.setCustDriverprefGender(entity.getCustDriverprefGender());
		res.setCustDriverprefIssmoker(entity.getCustDriverprefIssmoker());
		res.setCustDriverprefMusictaste(entity.getCustDriverprefMusictaste());
		res.setCustEmail(entity.getCustEmail());
		res.setCustFirstname(entity.getCustFirstname());
		res.setCustFixedphoneno(entity.getCustFixedphoneno());
		res.setCustGender(entity.getCustGender());
		res.setCustGroup(entity.getCustGroup());
		res.setCustIssmoker(entity.getCustIssmoker());
		res.setCustLastCheck(entity.getCustLastCheck());
		res.setCustLastname(entity.getCustLastname());
		res.setCustLastMatchingChange(entity.getCustLastMatchingChange());
		res.setCustLicensedate(entity.getCustLicensedate());
		res.setCustPostident(entity.getCustPostident());
		res.setCustMobilephoneno(entity.getCustMobilephoneno());
		res.setCustNickname(entity.getCustNickname());
		//
		// currently not supported
		//
		//res.setCustPostident(custPostident);
		//res.setCustPresencemssg(custPresencemssg)
		//res.setCustProfilepic(custProfilepic);
		res.setCustRegistrdate(entity.getCustRegistrdate());
		res.setCustRiderprefAge(entity.getCustRiderprefAge());
		res.setCustRiderprefGender(entity.getCustRiderprefGender());
		res.setCustRiderprefIssmoker(entity.getCustRiderprefIssmoker());
		res.setCustRiderprefMusictaste(entity.getCustRiderprefMusictaste());
		//res.setCustSessionId(entity.getCustSessionId());
		//
		
		return res;
	}

}
