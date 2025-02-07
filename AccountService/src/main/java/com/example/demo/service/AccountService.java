package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.exceptions.AccountNotFound;
import com.example.demo.exceptions.InsufficientBalance;

public interface AccountService {
	public String createAccount(Account account);

	public Account updateAccount(Account account) throws AccountNotFound;

	public String closeAccount(int accountNo) throws AccountNotFound;

	public Account getAccountDetails(int accountNo) throws AccountNotFound;

	public double withdraw(int accNo, double amountToWithdraw) throws AccountNotFound, InsufficientBalance;

	public double deposit(int accNo, double amountToDeposit) throws AccountNotFound;

	public double fundTransfer(int fromAccNo, int toAccNo, double amountToTransfer)
			throws AccountNotFound, InsufficientBalance;
}
