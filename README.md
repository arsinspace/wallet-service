# Wallet-Service:
The service helps in creating a user with wallet (default balance = 0.0), credit funds to the wallet, deposit funds from wallet, maintain transaction details, login as Admin and view all users and users actions

# Prerequisites:
- Java 17 or above
- Maven

# Installation:
- Clone this repo:
```bash
> gh repo clone arsinspace/wallet-service
> cd WalletService
> For migration DB use - mvn liquibase:update
```
- Install Maven.
- Update the dependencies given in the `pom.xml` using Maven.
- Run WalletServiceApplication.java in the IDE.
# Sample API end points:
```
Register new User. Create user will automatically 
create an wallet,
End point = /register
JSON body = {"name":"Adam",
	     "lastName":"Adam",
             "age":"29",
             "credentials":
              { "login":"adam",
                "password":"123"
		}}
```
```
Login to system. If system doesn't know you, 
you will be automatically redirected to the 
registration page.
End point = /login
JSON body = {"login":"adam",
	     "password":"123"
             }
```
```
Logout from system
End point = /logout
```
```
View current user transactional history
End point = /history
```
```
View current user wallet balance
End point = /wallet
```
```
Work with transaction
End point = /transaction
```
```
Credit money to the user's wallet,
End point = /credit
Transaction ID must be unique
JSON body = {"transactionalId":"yourId",
	     "purpose":"example",
             "amount":123}
             }
```
```
Debit money from the user's wallet,
End point = /debit
Transaction ID must be unique
JSON body = {"transactionalId":"yourId",
	     "purpose":"example",
             "amount":123
             }
```
```
Login as admin (admin user don't save in memory)
End point = /login
Type JSON body = {"login":"admin",
	          "password":"admin"
}
```
```
View all registered users
End point = /users
```
```
View users actions
End point = /auditor
```

# Contribute:
* Corrections & Contributions are always welcome! 
