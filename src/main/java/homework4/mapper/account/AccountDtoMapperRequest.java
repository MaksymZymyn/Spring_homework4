package homework4.mapper.account;

import homework4.domain.bank.*;
import homework4.domain.dto.account.AccountDtoRequest;
import homework4.mapper.DtoMapperFacade;
import org.springframework.stereotype.Service;

@Service
public class AccountDtoMapperRequest extends DtoMapperFacade<Account, AccountDtoRequest> {
    public AccountDtoMapperRequest() {
        super(Account.class, AccountDtoRequest.class);
    }
}
