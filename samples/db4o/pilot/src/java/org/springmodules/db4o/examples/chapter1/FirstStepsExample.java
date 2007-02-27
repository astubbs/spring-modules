package org.springmodules.db4o.examples.chapter1;

import java.util.Iterator;

import org.springmodules.db4o.examples.Pilot;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * This sample is a direct port of the first example discussed in the Db4o
 * tutorial that ships with Db4o-4.6. However, the main difference here is that
 * the springified version obtains the object container via dependency
 * injection. The usage of the object container stays the same. Therefore, this
 * is example is rather uninteresting.
 * 
 * @author Daniel Mitterdorfer.
 * 
 */
public class FirstStepsExample {
	public static void storeFirstPilot(ObjectContainer db) {
		Pilot pilot1 = new Pilot("Michael Schumacher", 100);
		db.set(pilot1);
		System.out.println("Stored " + pilot1);
	}

	public static void storeSecondPilot(ObjectContainer db) {
		Pilot pilot2 = new Pilot("Rubens Barrichello", 99);
		db.set(pilot2);
		System.out.println("Stored " + pilot2);
	}

	public static void retrieveAllPilots(ObjectContainer db) {
		Pilot proto = new Pilot(null, 0);
		ObjectSet result = db.get(proto);
		listResult(result);
	}

	public static void listResult(ObjectSet result) {
		for (Iterator it = result.iterator(); it.hasNext();) {
			Pilot pilot = (Pilot) it.next();
			System.out.println(pilot.toString());
		}
	}

	public static void retrievePilotByName(ObjectContainer db) {
		Pilot proto = new Pilot("Michael Schumacher", 0);
		ObjectSet result = db.get(proto);
		listResult(result);
	}

	public static void retrievePilotByExactPoints(ObjectContainer db) {
		Pilot proto = new Pilot(null, 100);
		ObjectSet result = db.get(proto);
		listResult(result);
	}

	public static void updatePilot(ObjectContainer db) {
		ObjectSet result = db.get(new Pilot("Michael Schumacher", 0));
		Pilot found = (Pilot) result.next();
		found.addPoints(11);
		db.set(found);
		System.out.println("Added 11 points for " + found);
		retrieveAllPilots(db);
	}

	public static void deleteFirstPilotByName(ObjectContainer db) {
		ObjectSet result = db.get(new Pilot("Michael Schumacher", 0));
		Pilot found = (Pilot) result.next();
		db.delete(found);
		System.out.println("Deleted " + found);
		retrieveAllPilots(db);
	}

	public static void deleteSecondPilotByName(ObjectContainer db) {
		ObjectSet result = db.get(new Pilot("Rubens Barrichello", 0));
		Pilot found = (Pilot) result.next();
		db.delete(found);
		System.out.println("Deleted " + found);
		retrieveAllPilots(db);
	}

	/**
	 * @param args
	 *            Command line arguments. This appliation does not check any of
	 *            these arguments.
	 */
	public static void main(String[] args) {
		AbstractApplicationContext ctx = ExampleUtils.getContext();
		ObjectContainer db = (ObjectContainer) ctx.getBean(ExampleUtils.CONTAINER_BEAN_ID);
		try {
			storeFirstPilot(db);
			storeSecondPilot(db);
			retrieveAllPilots(db);
			retrievePilotByName(db);
			retrievePilotByExactPoints(db);
			updatePilot(db);
			deleteFirstPilotByName(db);
			deleteSecondPilotByName(db);

		} finally {
			//closes the database file implicitly
			ctx.close();
		}
	}
}
