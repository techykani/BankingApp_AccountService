package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Account;
import com.example.demo.exceptions.AccountNotFound;
import com.example.demo.exceptions.InsufficientBalance;
import com.example.demo.service.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	@Autowired
	AccountService service;

	@PostMapping("/create")
	public String saveAccount(@RequestBody Account account) {
		return service.createAccount(account);
	}

	@PutMapping("/update")
	public Account updateAccount(@RequestBody Account account) throws AccountNotFound {
		return service.updateAccount(account);
	}

	@DeleteMapping("/delete/{accno}")
	public String deleteAccount(@PathVariable("accno") int accountNo) throws AccountNotFound {
		return service.closeAccount(accountNo);
	}

	@GetMapping("/getAccount/{accno}")//http://localhost:8080/accounts/getAccount/1
	public Account getAccountDetails(@PathVariable("accno") int accountNo) throws AccountNotFound {
		return service.getAccountDetails(accountNo);
	}

	@PutMapping("/withdraw/{accNo}/{amount}")//http://localhost:8080/accounts/withdraw/1/12000
	public double withdraw(@PathVariable("accNo") int accNo, @PathVariable("amount") double amountToWithdraw)
			throws AccountNotFound, InsufficientBalance {
		return service.withdraw(accNo, amountToWithdraw);
	}

	@PutMapping("/deposit/{accNo}/{amount}")//http://localhost:8080/accounts/deposit/1/13000
	public double deposit(@PathVariable("accNo") int accNo, @PathVariable("amount") double amountToDeposit)
			throws AccountNotFound, InsufficientBalance {
		return service.deposit(accNo, amountToDeposit);
	}

	@PutMapping("/transfer/{fromAccNo}/{toAccNo}/{amountToTransfer}")//http://localhost:8080/accounts/transfer/1/2/1000
	public double fundTransfer(@PathVariable("fromAccNo") int fromAccNo, @PathVariable("toAccNo") int toAccNo,
			@PathVariable("amountToTransfer") double amountToTransfer) throws AccountNotFound, InsufficientBalance {
		return service.fundTransfer(fromAccNo, toAccNo, amountToTransfer);
	}
}
