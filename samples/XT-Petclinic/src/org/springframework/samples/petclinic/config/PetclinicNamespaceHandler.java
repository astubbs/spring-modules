package org.springframework.samples.petclinic.config;

import java.util.Set;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedSet;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.Visit;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author <a href="mailto:irbouh@gmail.com">Omar Irbouh</a>
 * @since Aug 24, 2006
 */
public class PetclinicNamespaceHandler extends NamespaceHandlerSupport {

	// reference suffix
	public static final String REF_SUFFIX = "-ref";

	// elements
	public static final String PETTYPE_ELEMENT = "type";
	public static final String SPECIALTY_ELEMENT = "specialty";
	public static final String VISIT_ELEMENT = "visit";
	public static final String PET_ELEMENT = "pet";
	public static final String OWNER_ELEMENT = "owner";
	public static final String VET_ELEMENT = "vet";

	// attributes
	public static final String ID_ATTRIBUTE = "id";

	public void init() {
		registerBeanDefinitionParser(PETTYPE_ELEMENT, new PetclinicBeanDefinitionParser(PetType.class));
		registerBeanDefinitionParser(SPECIALTY_ELEMENT, new PetclinicBeanDefinitionParser(Specialty.class));
		registerBeanDefinitionParser(VISIT_ELEMENT, new PetclinicBeanDefinitionParser(Visit.class));
		registerBeanDefinitionParser(PET_ELEMENT, new PetclinicBeanDefinitionParser(PetFactory.class, "visits"));
		registerBeanDefinitionParser(OWNER_ELEMENT, new PetclinicBeanDefinitionParser(OwnerFactory.class, "pets"));
		registerBeanDefinitionParser(VET_ELEMENT, new PetclinicBeanDefinitionParser(VetFactory.class, "specialties"));
	}

	public static class PetclinicBeanDefinitionParser implements BeanDefinitionParser {

		private Class beanClass;
		private String childrenPropertyName;

		public PetclinicBeanDefinitionParser(Class beanClass) {
			this.beanClass = beanClass;
		}

		public PetclinicBeanDefinitionParser(Class beanClass, String childrenPropertyName) {
			this.beanClass = beanClass;
			this.childrenPropertyName = childrenPropertyName;
		}

		public BeanDefinition parse(Element element, ParserContext parserContext) {
			BeanDefinitionRegistry registry = parserContext.getRegistry();

			// create property values
			MutablePropertyValues pvs = new MutablePropertyValues();
			NamedNodeMap attributes = element.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr attribute = (Attr) attributes.item(i);
				String name = attribute.getLocalName();
				String value = attribute.getValue();

				// is it a property value or reference?
				if (name.endsWith(REF_SUFFIX)) {
					// create and register a reference
					String refName = name.substring(0, name.length() - REF_SUFFIX.length());
					RuntimeBeanReference reference = new RuntimeBeanReference(refName + "-" + value);
					pvs.addPropertyValue(refName, reference);
				} else {
					pvs.addPropertyValue(name, value);
				}
			}

			// create a new bean definition
			BeanDefinition definition = new RootBeanDefinition(getBeanClass(), pvs);
			BeanDefinitionHolder holder = new BeanDefinitionHolder(definition, resolveBeanId(element));

			// register bean definition
			BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

			// allow for further processing
			if (StringUtils.hasText(this.childrenPropertyName)) {
				processChildren(parserContext, element, definition);
			}

			return definition;
		}

		protected void processChildren(ParserContext parserContext, Element element, BeanDefinition definition) {
			NodeList nodes = element.getChildNodes();

			// ManagedSet contains references that will be resolved when setting properties
			Set<RuntimeBeanReference> children = new ManagedSet();

			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node instanceof Element) {
					String id = resolveBeanId(((Element) node));
					children.add(new RuntimeBeanReference(id));
				}
			}

			// register the children
			if (!CollectionUtils.isEmpty(children)) {
				definition.getPropertyValues().addPropertyValue(this.childrenPropertyName, children);
			}
		}

		protected Class getBeanClass() {
			return beanClass;
		}

		protected String resolveBeanName(Element element) {
			return element.getLocalName();
		}

		protected String resolveBeanId(Element element) {
			return resolveBeanName(element) + '-' + element.getAttribute(ID_ATTRIBUTE);
		}

	}

}