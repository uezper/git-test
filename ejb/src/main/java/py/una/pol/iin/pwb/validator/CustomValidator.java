package py.una.pol.iin.pwb.validator;

import java.util.Iterator;
import java.util.Set;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import py.una.pol.iin.pwb.exception.InvalidFormatException;

@Stateless
public class CustomValidator {

	
	public static <T> String validateAndGetMessage(T object) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
	    
	    Set<ConstraintViolation<T>> constraintViolations = 
	    		validator.validate( object );
	    
	    String message = "";
	    String separator = "";
	    if (constraintViolations.size() > 0) 
	    {	    	
	    	Iterator<ConstraintViolation<T>> it = constraintViolations.iterator();
	    	while (it.hasNext())
	    	{
	    		message += separator + it.next().getMessage();
	    		separator = "; ";
	    	}
	    		   
	    }
	    return message;
	}
	
	public static <T> void validateAndThrow(T object) throws InvalidFormatException {
		
		String message = validateAndGetMessage(object);
		if (!message.isEmpty()) {
			throw new InvalidFormatException(message);
		}		
	}
}
