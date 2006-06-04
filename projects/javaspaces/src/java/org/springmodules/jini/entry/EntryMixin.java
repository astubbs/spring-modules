/**
 * Created on Mar 12, 2006
 *
 * $Id$
 * $Revision$
 */
package org.springmodules.jini.entry;

import net.jini.core.entry.Entry;

import org.springframework.aop.support.DelegatingIntroductionInterceptor;

/**
 * Mixin which converts standard POJO into JavaSpace compliant Entry. For now only the Entry interface is added but in the future more magic can be injected
 * like converting getter/setters into public fields. Unfortunatelly this class creates some problems inside the space since the classes are synthetic and 
 * can't be downloaded by clients.
 * 
 * 
 * @author Costin Leau
 *
 */
public class EntryMixin extends DelegatingIntroductionInterceptor implements Entry {

}
