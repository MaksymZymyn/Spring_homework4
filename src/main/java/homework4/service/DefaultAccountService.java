package homework4.service;

import homework4.dao.AccountRepository;
import homework4.domain.bank.Account;
import homework4.exceptions.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DefaultAccountService implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void delete(Account account) {
        accountRepository.delete(account);
    }

    @Override
    public void deleteAll(List<Account> accounts) {
        accountRepository.deleteAll(accounts);
    }

    @Override
    public void saveAll(List<Account> accounts) {
        accountRepository.saveAll(accounts);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = NoSuchElementException.class, timeout = 1000)
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with ID " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Account getByNumber(String accountNumber) {
        try {
            UUID number = UUID.fromString(accountNumber.toString());
            return accountRepository.findByNumber(number)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with number " + accountNumber));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account number format");
        }
    }

    @Override
    public Account deposit(String accountNumber, double amount) {
        try {
            UUID number = UUID.fromString(accountNumber.toString());
            Account account = accountRepository.findByNumber(number)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with number " + accountNumber));

            if (amount <= 0) {
                throw new IllegalArgumentException("Deposit amount must be greater than zero");
            }

            double balance = account.getBalance();
            account.setBalance(balance + amount);
            accountRepository.save(account);
            return account;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account number format");
        }
    }

    @Override
    @Transactional(rollbackFor = IllegalArgumentException.class, timeout = 1000)
    public Account withdraw(String number, double amount) {
        try {
            UUID accountNumber = UUID.fromString(number.toString());
            Account account = accountRepository.findByNumber(accountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with number " + number));

            if (amount <= 0) {
                throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
            }

            double balance = account.getBalance();
            if (balance < amount - 1000) {
                throw new  InsufficientBalanceException("Insufficient balance in the account");
            }

            account.setBalance(balance - amount);
            log.info("Withdrawn {} from account with number {}", amount, accountNumber);
            accountRepository.save(account);
            return account;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account number format");
        }
    }

    @Override
    public void transfer(String fromNumber, String toNumber, double amount) {
        try {
            UUID fromAccountNumber = UUID.fromString(fromNumber.toString());
            UUID toAccountNumber = UUID.fromString(toNumber.toString());
            Account fromAccount = accountRepository.findByNumber(fromAccountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with number " + fromNumber));

            Account toAccount = accountRepository.findByNumber(toAccountNumber)
                    .orElseThrow(() -> new AccountNotFoundException("Account not found with number " + toNumber));

            if (fromAccountNumber.equals(toAccountNumber)) {
                throw new SameAccountException("From and To account numbers cannot be the same");
            }

            if (amount <= 0) {
                throw new InvalidTransferAmountException("Transfer amount must be greater than 0");
            }

            double balance = fromAccount.getBalance();
            if (balance < amount - 1000) {
                throw new InsufficientBalanceException("Insufficient balance in the from account");
            }

            fromAccount.setBalance(balance - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account number format");
        }
    }
}
