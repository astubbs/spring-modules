/*
 * Copyright (c) 2005 JTeam B.V.
 * www.jteam.nl
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * JTeam B.V. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with JTeam.
 */

package org.springmodules.dwr.create;

import junit.framework.TestCase;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import uk.ltd.getahead.dwr.ExecutionContext;

/**
 * @author Bram Smeets
 */
public class SpringContextCreatorTests extends TestCase {
    public SpringContextCreatorTests(String name) {
        super(name);
    }

    public void testInit() throws Exception {
        SpringWebContextCreator creator = new SpringWebContextCreator();

        Element element = createElement();

        try {
            creator.init(element);
        } catch(IllegalArgumentException e) {
            // do nothing, was expected
        }

        element.setAttribute("beanName", "aBeanName");
        creator.init(element);
    }

    public void testGetType() throws Exception {
        SpringWebContextCreator creator = new SpringWebContextCreator();

        Element element = createElement();
        element.setAttribute("beanName", "testBean");
        creator.init(element);

        //assertNotNull(creator.getType());
    }

    private Element createElement() throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder domBuilder = domFactory.newDocumentBuilder();

        Document document = domBuilder.newDocument();
        return document.createElement("element");
    }
}