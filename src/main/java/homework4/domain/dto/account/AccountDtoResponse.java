package homework4.domain.dto.account;

import homework4.domain.bank.Currency;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDtoResponse {
    private Long id;
    private UUID number;
    private Currency currency;
    private double balance;
    private String customerName;
    private String creationByUserName;
    private LocalDateTime creationDate;
    private String lastModifiedByUserName;
    private LocalDateTime lastModifiedDate;
}
