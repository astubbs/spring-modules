package org.springmodules.validation;

import java.io.StringReader;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.predicates.ValidationRule;
import org.springmodules.validation.valang.ParseException;
import org.springmodules.validation.valang.ValangParser;
import org.springmodules.validation.valang.ValangVisitor;

/**
 * <p>ValangValidatorFactoryBean takes a Valang syntax and returns a
 * org.springframework.validation.Validator instance. This instance is
 * a singleton and is thread-safe.
 * 
 * <p>A custom visitor can be registered to use custom functions in the Valang syntax.
 * 
 * <p>Custom property editors can be registered using org.springmodules.validation.CustomPropertyEditor.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class ValangValidatorFactoryBean implements FactoryBean, InitializingBean {

	private String valang = null;
	private Validator validator = null;
	private ValangVisitor visitor = null;
	private Collection customPropertyEditors = null;
	
	public ValangValidatorFactoryBean() {
		super();
	}

	/**
	 * <p>This property sets the Valang syntax.
	 * 
	 * @param valang the Valang syntax
	 */
	public void setValang(String valang) {
		this.valang = valang;
	}
	
	/**
	 * <p>This property takes a custom visitor with custom functions.
	 * 
	 * @param visitor the custom visitor;
	 */
	public void setVisitor(ValangVisitor visitor) {
		this.visitor = visitor;
	}
	
	/**
	 * <p>Sets custom property editors on BeanWrapper instances (optional).
	 * 
	 * @param customPropertyEditors the custom editors.
	 */
	public void setCustomPropertyEditors(Collection customPropertyEditors) {
		this.customPropertyEditors = customPropertyEditors;
	}
	
	private Collection getCustomPropertyEditors() {
		return this.customPropertyEditors;
	}
	
	private String getValang() {
		return this.valang;
	}
	
	public Object getObject() throws Exception {
		return this.validator;
	}

	public Class getObjectType() {
		return Validator.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		if (!StringUtils.hasLength(getValang())) {
			throw new IllegalArgumentException("[valang] property must be set!");
		}
		
		
		this.validator = new Validator() {
			private Collection rules = null;

			{
				try {
					ValangParser parser = new ValangParser(new StringReader(getValang()));
					parser.getVisitor().setVisitor(visitor);
					rules = parser.parseValidation();
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}		
			}
			
			public boolean supports(Class clazz) {
				return true;
			}
			
			public void validate(Object target, Errors errors) {
				BeanWrapper beanWrapper = null;
				if (target instanceof BeanWrapper) {
					beanWrapper = (BeanWrapper)target;
				} else {
					beanWrapper = new BeanWrapperImpl(target);
				}
				if (getCustomPropertyEditors() != null) {
					for (Iterator iter = getCustomPropertyEditors().iterator(); iter.hasNext();) {
						CustomPropertyEditor customPropertyEditor = (CustomPropertyEditor)iter.next();
						if (customPropertyEditor.getRequiredType() == null) {
							throw new IllegalArgumentException("[requiredType] is required on CustomPropertyEditor instances!");
						} else if (customPropertyEditor.getPropertyEditor() == null) {
							throw new IllegalArgumentException("[propertyEditor] is required on CustomPropertyEditor instances!");
						}
						if (StringUtils.hasLength(customPropertyEditor.getPropertyPath())) {
							beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(), customPropertyEditor.getPropertyPath(), customPropertyEditor.getPropertyEditor());
						} else {
							beanWrapper.registerCustomEditor(customPropertyEditor.getRequiredType(), customPropertyEditor.getPropertyEditor());
						}
					}
				}
				
				for (Iterator iter = rules.iterator(); iter.hasNext();) {
					ValidationRule rule = (ValidationRule)iter.next();
					rule.validate(beanWrapper, errors);
				}
			}
		};
	}

}
