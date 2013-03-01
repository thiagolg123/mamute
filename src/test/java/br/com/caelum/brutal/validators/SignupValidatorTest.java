package br.com.caelum.brutal.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutal.dao.UserDAO;
import br.com.caelum.brutal.model.User;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.util.test.JSR303MockValidator;

public class SignupValidatorTest {

    private UserDAO users;
    private Validator validator;
    private SignupValidator signupValidator;
	private UserValidator userValidator;
	private EmailValidator emailValidator;
    
    @Before
    public void setup() {
        users = mock(UserDAO.class);
        validator = new JSR303MockValidator();
        emailValidator = new EmailValidator(validator, users);
        userValidator = new UserValidator(validator, emailValidator);
        signupValidator = new SignupValidator(validator, userValidator);
    }

    @Test
    public void should_verify_email() {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(true);
        User user = new User("nome muito grande ai meu deus", "used@gmail.com", "123456");
        boolean valid = signupValidator.validate(user, "123456", "123456");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_email_without_domain() {
    	User user = new User("nome muito grande ai meu deus", "usedgmail.com", "123456");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_passwords() throws Exception {
        when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
        User user = new User("nome muito grande ai meu deus", "valid@gmail.com", "123456");
        boolean valid = signupValidator.validate(user, "123456", "1234567");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_verify_tiny_password() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	User user = new User("nome muito grande ai meu deus", "valid@gmail.com", "123");
    	boolean valid = signupValidator.validate(user, "123", "123");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_password() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	String password = 666*100 + "";
    	User user = new User("nome", "valid@gmail.com", password);
    	boolean valid = signupValidator.validate(user, password, password);
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_tiny_name() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	User user = new User("nome", "valid@gmail.com", "123456");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_large_name() throws Exception {
    	when(users.existsWithEmail("valid@gmail.com")).thenReturn(false);
    	String name = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    	User user = new User(name, "valid@gmail.com", "123456");
    	boolean valid = signupValidator.validate(user, "123456", "123456");
    	
    	assertFalse(valid);
    }
    
    @Test
    public void should_verify_null() throws Exception {
        boolean valid = signupValidator.validate(null, "123", "1234");
        
        assertFalse(valid);
    }
    
    @Test
    public void should_valid_user() throws Exception {
        when(users.existsWithEmail("used@gmail.com")).thenReturn(false);
        User user = new User("nome muito grande ai meu deus", "used@gmail.com", "123456");
        boolean valid = signupValidator.validate(user, "123456", "123456");
        
        assertTrue(valid);
    }

}
