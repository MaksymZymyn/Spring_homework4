package homework4.resource;

import homework4.domain.SysUser;
import homework4.domain.bank.Account;
import homework4.domain.bank.Currency;
import homework4.domain.bank.Customer;
import homework4.domain.dto.account.AccountDtoResponse;
import homework4.exceptions.AccountNotFoundException;
import homework4.mapper.account.AccountDtoMapperResponse;
import homework4.service.AccountService;
import homework4.service.jwt.JwtProvider;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountDtoMapperResponse accountDtoMapper;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        when(jwtProvider.validateAccessToken(any(String.class))).thenReturn(true);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() {
        reset(accountService, accountDtoMapper, jwtProvider);
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testFindAll() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        List<Account> accounts = new ArrayList<>();

        Account firstAccount = new Account();
        firstAccount.setId(10L);
        firstAccount.setCurrency(Currency.USD);
        UUID firstAccountNumber = UUID.fromString("77cf9522-5d7e-4928-a6b5-50c1160e9bd3");
        firstAccount.setNumber(firstAccountNumber);
        String customerNameFirstAccount = "first";
        Customer customer1 = new Customer(customerNameFirstAccount, "ava@example.com", 38, "789654321", "password12");
        firstAccount.setCustomer(customer1);

        Account secondAccount = new Account();
        secondAccount.setId(20L);
        secondAccount.setCurrency(Currency.EUR);
        UUID secondAccountNumber = UUID.fromString("77cf9522-5d7e-4928-a6b5-50c1160e9bd3");
        firstAccount.setNumber(secondAccountNumber);
        String customerNameSecondAccount = "second";
        Customer customer2 = new Customer(customerNameSecondAccount, "john@example.com", 35, "123454321", "password45");
        secondAccount.setCustomer(customer2);

        accounts.add(firstAccount);
        accounts.add(secondAccount);

        AccountDtoResponse dtoFirst = new AccountDtoResponse(10L, firstAccountNumber, Currency.USD, 1000.0,
                customerNameFirstAccount, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());
        AccountDtoResponse dtoSecond = new AccountDtoResponse(20L, secondAccountNumber, Currency.EUR, 2000.0,
                customerNameSecondAccount, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());

        when(accountService.findAll()).thenReturn(accounts);
        when(accountDtoMapper.convertToDto(firstAccount)).thenReturn(dtoFirst);
        when(accountDtoMapper.convertToDto(secondAccount)).thenReturn(dtoSecond);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(dtoFirst.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].number", Matchers.is(dtoFirst.getNumber().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName", Matchers.is(dtoFirst.getCustomerName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance", Matchers.is(dtoFirst.getBalance())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currency", Matchers.is(dtoFirst.getCurrency().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(dtoSecond.getId().intValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].number", Matchers.is(dtoSecond.getNumber().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerName", Matchers.is(dtoSecond.getCustomerName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].balance", Matchers.is(dtoSecond.getBalance())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].currency", Matchers.is(dtoSecond.getCurrency().toString())));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetByNumber_Success() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        Account account = new Account();
        account.setId(10L);
        UUID accountNumber = UUID.randomUUID();
        account.setNumber(accountNumber);
        String customerName = "first";
        Customer customer = new Customer(customerName, "ava@example.com", 38, "789654321", "password12");
        account.setCustomer(customer);

        AccountDtoResponse dtoFirst = new AccountDtoResponse(10L, accountNumber, Currency.USD, 1000.0,
                customerName, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());

        when(accountService.getByNumber(anyString())).thenReturn(account);
        when(accountDtoMapper.convertToDto(account)).thenReturn(dtoFirst);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{number}", accountNumber.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testGetByNumber_AccountNotFound() throws Exception {
        UUID number = UUID.randomUUID();
        when(accountService.getByNumber(anyString())).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{number}", number))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testDeposit_Success() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        Account account = new Account();
        account.setId(10L);
        UUID accountNumber = UUID.randomUUID();
        account.setNumber(accountNumber);
        String customerName = "first";
        Customer customer = new Customer(customerName, "ava@example.com", 38, "789654321", "password12");
        account.setCustomer(customer);

        AccountDtoResponse dtoResponse = new AccountDtoResponse(10L, accountNumber, Currency.USD, 1000.0,
                customerName, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());

        when(accountService.deposit(anyString(), anyDouble())).thenReturn(account);
        when(accountDtoMapper.convertToDto(account)).thenReturn(dtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/deposit/{accountNumber}", accountNumber.toString())
                        .param("amount", "1000.0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(1000.0)));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testWithdraw_Success() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        String creatorName = "creatorUser";
        creator.setUserName(creatorName);

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        String modifierName = "modifierUser";
        modifier.setUserName(modifierName);

        Account account = new Account();
        account.setId(10L);
        UUID accountNumber = UUID.randomUUID();
        account.setBalance(2000);
        account.setNumber(accountNumber);
        String customerName = "first";
        Customer customer = new Customer(customerName, "ava@example.com", 38, "789654321",
                "password12");
        account.setCustomer(customer);

        AccountDtoResponse dtoResponse = new AccountDtoResponse(10L, accountNumber, Currency.USD, 1000.0,
                customerName, creatorName, LocalDateTime.now(), modifierName, LocalDateTime.now());

        when(accountService.withdraw(anyString(), anyDouble())).thenReturn(account);
        when(accountDtoMapper.convertToDto(account)).thenReturn(dtoResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/withdrawal/{accountNumber}", accountNumber.toString())
                        .param("amount", "1000.0"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", Matchers.is(1000.0)));
    }

    @Test
    @WithMockUser(username = "testUser")
    public void testTransfer_Success() throws Exception {
        SysUser creator = new SysUser();
        creator.setId(1L);
        creator.setUserName("creatorUser");

        SysUser modifier = new SysUser();
        modifier.setId(2L);
        modifier.setUserName("modifierUser");

        Account fromAccount = new Account();
        fromAccount.setId(10L);
        UUID fromAccountNumber = UUID.randomUUID();
        fromAccount.setBalance(2000);
        fromAccount.setNumber(fromAccountNumber);
        String fromCustomerName = "first";
        Customer fromCustomer = new Customer(fromCustomerName, "ava@example.com", 38, "789654321",
                "password12");
        fromAccount.setCustomer(fromCustomer);

        Account toAccount = new Account();
        toAccount.setId(20L);
        UUID toAccountNumber = UUID.randomUUID();
        toAccount.setBalance(500);
        toAccount.setNumber(toAccountNumber);
        String toCustomerName = "second";
        Customer toCustomer = new Customer(toCustomerName, "john@example.com", 35, "123454321", "password45");
        toAccount.setCustomer(toCustomer);

        double transferAmount = 1000.0;

        doAnswer(invocation -> {
            String from = invocation.getArgument(0);
            String to = invocation.getArgument(1);
            double amount = invocation.getArgument(2);
            Account fromAcc = accountService.getByNumber(from);
            Account toAcc = accountService.getByNumber(to);
            double fromBalance = fromAcc.getBalance();
            double toBalance = toAcc.getBalance();
            fromAcc.setBalance(fromBalance - amount);
            toAcc.setBalance(toBalance + amount);
            return null;
        }).when(accountService).transfer(anyString(), anyString(), anyDouble());

        when(accountService.getByNumber(String.valueOf(fromAccountNumber))).thenReturn(fromAccount);
        when(accountService.getByNumber(String.valueOf(toAccountNumber))).thenReturn(toAccount);

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/transfer/{fromAccountNumber}/{toAccountNumber}",
                                fromAccountNumber.toString(), toAccountNumber.toString())
                        .param("amount", String.valueOf(transferAmount)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fromAccountBalance", Matchers.is(1000.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.toAccountBalance", Matchers.is(1500.0)));
    }
}
