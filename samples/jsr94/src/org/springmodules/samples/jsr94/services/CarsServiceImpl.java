/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.springmodules.samples.jsr94.services;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.rules.InvalidRuleSessionException;
import javax.rules.StatefulRuleSession;
import javax.rules.StatelessRuleSession;

import org.springmodules.jsr94.core.Jsr94Template;
import org.springmodules.jsr94.support.StatefulRuleSessionCallback;
import org.springmodules.jsr94.support.StatelessRuleSessionCallback;
import org.springmodules.samples.jsr94.daos.CarsDAO;
import org.springmodules.samples.jsr94.model.Car;

/**
 * Service implementation to get the list of cars and test the
 * JSR94 support in different scenarios
 * 
 * @author Thierry Templier
 */
public class CarsServiceImpl implements CarsService {
	public final static String CARS_RULE_URI="cars";

	private Jsr94Template template;
	private CarsDAO carsDAO; 

	private void showListCars(List cars) {
		for(Iterator i=cars.iterator();i.hasNext();) {
			Object o=i.next();
			System.out.println("## o = "+o+" - "+Car.class.toString());
			Car car=(Car)o;
			System.out.println(" - goodBargain = "+car.isGoodBargain());
		}
	}
	
	public void testStateless() {
		final List cars=carsDAO.getCars();
		getTemplate().executeStateless("cars",null,new StatelessRuleSessionCallback() {
			public Object execute(StatelessRuleSession session) throws InvalidRuleSessionException, RemoteException {
				return session.executeRules(cars);
			}
		});
		showListCars(cars);
	}

	public void testStateful() {
		final List cars=carsDAO.getCars();
		List modifiedCars=(List)getTemplate().executeStateful(CARS_RULE_URI,null,new StatefulRuleSessionCallback() {
			public Object execute(StatefulRuleSession statefulRuleSession) throws InvalidRuleSessionException,RemoteException {
				statefulRuleSession.addObjects(cars);
				statefulRuleSession.executeRules();
				return statefulRuleSession.getObjects();
			}
		});
		showListCars(modifiedCars);
	}

	/**
	 * @see org.springmodules.samples.jrules.services.CarsService#getGoodBargainCars()
	 */
	public List getGoodBargainCars() {
		final List cars=carsDAO.getCars();
		getTemplate().executeStateless(CARS_RULE_URI,null,new StatelessRuleSessionCallback() {
			public Object execute(StatelessRuleSession session) throws InvalidRuleSessionException, RemoteException {
				return session.executeRules(cars);
			}
		});
		List goodBargainCars=new ArrayList();
		for(Iterator i=cars.iterator();i.hasNext();) {
			Car car=(Car)i.next();
			if( car.isGoodBargain() )
				goodBargainCars.add(car);
		}
		return goodBargainCars;
	}

	public Jsr94Template getTemplate() {
		return template;
	}

	public void setTemplate(Jsr94Template template) {
		this.template = template;
	}

	public CarsDAO getCarsDAO() {
		return carsDAO;
	}

	public void setCarsDAO(CarsDAO carsDAO) {
		this.carsDAO = carsDAO;
	}

}
