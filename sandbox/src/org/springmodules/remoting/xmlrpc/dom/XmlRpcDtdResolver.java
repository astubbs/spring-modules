/* 
 * Created on Jun 4, 2005
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2005 the original author or authors.
 */
package org.springmodules.remoting.xmlrpc.dom;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * <p>
 * Implementation of <code>{@link EntityResolver}</code> for the XML-RPC DTD,
 * to load the DTD from the Spring Modules class path (or JAR file).
 * </p>
 * <p>
 * Fetches "xml-rpc.dtd" from the class path resource
 * "/org/springmodules/remoting/xmlrpc/xml-rpc.dtd",
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 1.2 $ $Date: 2005/09/25 05:20:03 $
 */
public class XmlRpcDtdResolver implements EntityResolver {

  /**
   * Name of the DTD to use.
   */
  private static final String DTD_NAME = "xml-rpc";

  /**
   * Package where to search the DTD from.
   */
  private static final String SEARCH_PACKAGE = "/org/springmodules/remoting/xmlrpc/";

  /**
   * Message logger.
   */
  protected final Log logger = LogFactory.getLog(getClass());

  /**
   * Constructor.
   */
  public XmlRpcDtdResolver() {
    super();
  }

  /**
   * @see EntityResolver#resolveEntity(String, String)
   */
  public InputSource resolveEntity(String publicId, String systemId) {
    if (logger.isDebugEnabled()) {
      logger.debug("Trying to resolve XML entity with public ID [" + publicId
          + "] and system ID [" + systemId + "]");
    }
    if (systemId != null
        && systemId.indexOf(DTD_NAME) > systemId.lastIndexOf("/")) {
      String dtdFile = systemId.substring(systemId.indexOf(DTD_NAME));

      if (logger.isDebugEnabled()) {
        logger.debug("Trying to locate [" + dtdFile + "] under ["
            + SEARCH_PACKAGE + "]");
      }

      try {
        Resource resource = new ClassPathResource(SEARCH_PACKAGE + dtdFile,
            getClass());
        InputSource source = new InputSource(resource.getInputStream());
        source.setPublicId(publicId);
        source.setSystemId(systemId);

        if (logger.isDebugEnabled()) {
          logger.debug("Found beans DTD [" + systemId + "] in classpath");
        }
        return source;

      } catch (IOException ex) {
        if (logger.isDebugEnabled()) {
          logger.debug("Could not resolve beans DTD [" + systemId
              + "]: not found in class path", ex);
        }
      }
    }
    return null;
  }

}
