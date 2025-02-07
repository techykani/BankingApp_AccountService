package com.example.demo.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionDTO;
import com.example.demo.exceptions.AccountNotFound;
import com.example.demo.exceptions.InsufficientBalance;
import com.example.demo.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	AccountRepository repo;
	@Autowired
	RestTemplate restTemplate;

	@Override
	public String createAccount(Account account) {
		repo.save(account);
		return "Account Created Successfully";
	}

	@Override
	public Account updateAccount(Account account) throws AccountNotFound {

		return repo.save(account);
	}

	@Override
	public String closeAccount(int accountNo) throws AccountNotFound {
		repo.deleteById(accountNo);
		return "Account Deleted Successfully";
	}

	@Override
	public Account getAccountDetails(int accountNo) throws AccountNotFound {
		Optional<Account> optional = repo.findById(accountNo);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			throw new AccountNotFound("Enter Valid Account Number");
		}

	}

	@Override
	public double withdraw(int accNo, double amountToWithdraw) throws AccountNotFound, InsufficientBalance {
		Account account = getAccountDetails(accNo);
		double oldBalance = account.getAccBalance();
		if (oldBalance > amountToWithdraw) {
			double newBalance = oldBalance - amountToWithdraw;
			account.setAccBalance(newBalance);
			updateAccount(account);
			
			TransactionDTO transaction = new TransactionDTO();
			transaction.setTransactionId(123);
			transaction.setAccNo(accNo);
			transaction.setTimeOfTransaction(LocalDate.now());
			transaction.setTransactionType("withdraw");
			transaction.setUpdatedBalance(newBalance);
			
			String response = restTemplate.postForObject("http://localhost:8082/transactions/save", transaction,
					String.class);
			System.out.println("....." + response);
			return newBalance;
		} else
			throw new InsufficientBalance("Insuffcient Balance To Withdraw...");
	}

	@Override
	public double deposit(int accNo, double amountToDeposit) throws AccountNotFound {
		Account account = getAccountDetails(accNo);
		double oldBalance = account.getAccBalance();
		double newBalance = oldBalance + amountToDeposit;
		account.setAccBalance(newBalance);
		updateAccount(account);
		TransactionDTO transaction = new TransactionDTO();
		transaction.setAccNo(accNo);
		transaction.setTimeOfTransaction(LocalDate.now());
		transaction.setTransactionType("deposit");
		transaction.setUpdatedBalance(newBalance);

		return newBalance;
	}

	@Override
	public double fundTransfer(int fromAccNo, int toAccNo, double amountToTransfer)
			throws AccountNotFound, InsufficientBalance {
		double updatedBalnceFromAcc = withdraw(fromAccNo, amountToTransfer);
		deposit(toAccNo, amountToTransfer);
		return updatedBalnceFromAcc;
	}

}
