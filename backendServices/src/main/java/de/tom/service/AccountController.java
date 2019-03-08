package de.tom.service;

import de.tom.db.AccountRepository;
import de.tom.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.Optional;

@Controller // This means that this class is a Controller
@RequestMapping(path="/account") // This means URL's start with /account (after Application path)
public class AccountController {

	@Autowired // This means to get the bean called accountRepository
	// Which is auto-generated by Spring, we will use it to handle the data
	private AccountRepository accountRepository;

	@GetMapping("/exists/{name}") // Map ONLY GET Requests
	public ResponseEntity<Boolean> exists( @PathVariable("name") String name ) {
		boolean exists = accountRepository.exists( name );
		return exists ? ResponseEntity.ok( exists ) : ResponseEntity.status( HttpStatus.NOT_FOUND ).body( exists );
	}

	@GetMapping("/current/{name}") // Map ONLY GET Requests
	public ResponseEntity<Account> currentAccount( @PathVariable("name") String name ) {
		Account account = accountRepository.findByName( name );
		return ResponseEntity.ok( account );
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Account> account( @PathVariable("id") Long id ) {
		final Optional<Account> optionalAccount = accountRepository.findById( id );
		return optionalAccount.map( ResponseEntity::ok ).orElse( ResponseEntity.notFound().build() );
	}

	@DeleteMapping("/{name}")
	@Transactional
	public ResponseEntity delete( @PathVariable("name") String name ) {
		Account account = accountRepository.findByName( name );
		if( null == account ) {
			return ResponseEntity.notFound().build();
		}
		accountRepository.delete( account );
		return ResponseEntity.ok().build();
	}

	@PostMapping("/register")
	public ResponseEntity<Account> register( @RequestBody Account account ) {
		final Account savedAccount = accountRepository.save( account );
		return ResponseEntity.ok( savedAccount );
	}
}