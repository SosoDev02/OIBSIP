## ATMInterface
### Overview 
It is a console-based simulation application of an ATM machine that allows users to authenticate with a PIN and perform standard banking transactions.
### Tech Stack:
Java (console application)
### Feature Checklist:
- Startup prompt for User ID and PIN; deny access after 3 incorrect attempts
- Main menu displayed after successful login with the following options:
    - Transaction History — display a log of all past transactions in the current session
    - Withdraw — prompt for amount; validate sufficient balance; update balance; log transaction
    - Deposit — prompt for amount; update balance; log transaction
    - Transfer — prompt for recipient account ID and amount; validate balance; update both accounts; log transaction
    - Quit — display a goodbye message and exit
- Balance check before any withdrawal or transfer; display "Insufficient Funds" if balance is too low
- All transactions stored in an ArrayList and displayed clearly in Transaction History
- At least 5 distinct Java classes: ATM, Account, Transaction, Bank, Main
 
