package homework4.mapper.employer;

import homework4.domain.bank.Employer;
import homework4.domain.dto.employer.EmployerDtoRequest;
import homework4.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class EmployerDtoMapperRequest extends DtoMapperFacade<Employer, EmployerDtoRequest> {

    public EmployerDtoMapperRequest() {
        super(Employer.class, EmployerDtoRequest.class);
    }
}
