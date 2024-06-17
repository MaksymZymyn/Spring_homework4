package homework4.mapper.account;

import homework4.domain.bank.Account;
import homework4.domain.bank.Customer;
import homework4.domain.dto.account.AccountDtoResponse;
import homework4.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class AccountDtoMapperResponse extends DtoMapperFacade<Account, AccountDtoResponse> {

    public AccountDtoMapperResponse() {
        super(Account.class, AccountDtoResponse.class);
    }

    @Override
    protected void decorateDto(AccountDtoResponse dto, Account entity) {
        Customer customer = entity.getCustomer();
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found");
        }
        dto.setCustomerName(customer.getName());
    }
}
