package homework4.mapper.customer;

import homework4.domain.bank.Customer;
import homework4.domain.dto.customer.CustomerDtoRequest;
import homework4.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class CustomerDtoMapperRequest extends DtoMapperFacade<Customer, CustomerDtoRequest> {

    public CustomerDtoMapperRequest() {
        super(Customer.class, CustomerDtoRequest.class);
    }
}
