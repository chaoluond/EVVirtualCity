
package repastcity3.agent;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Math;
import repast.simphony.context.Context;
import org.geotools.referencing.datum.DefaultEllipsoid;
import com.vividsolutions.jts.awt.PointShapeFactory.Point;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import repast.simphony.space.gis.Geography;
import repastcity3.environment.Candidate1;
import repastcity3.environment.Residential;
import repastcity3.environment.Restaurant;
import repastcity3.environment.Route;
import repastcity3.environment.Shoppingcenter;
import repastcity3.environment.Supermarket;
import repastcity3.environment.Workplace;
import repastcity3.main.ContextManager;

public class DefaultAgent implements IAgent {

	private static Logger LOGGER = Logger.getLogger(DefaultAgent.class.getName());

	private Residential home; // Where the agent lives
	private Route route; // An object to move the agent around the world
	//private Route routeogn;
	private Route routedev1;
	private Route routedev2;
	private double routedisogn;
	private double routedisdev1;
	private double routedisdev2;
	private Coordinate origin;
	private Coordinate destination;

	private boolean goingHome = false; // Whether the agent is going to or from their home
	
	private boolean reachDestination = false;
	private static int uniqueID = 0;
	private final static double p1 = 0.272;
	private final static double p2 = 0.109;
	private final static double p3 = 0.187;
	private final static double p4 = 0.131;
	private int MAX = 16;
	private  double[] distances = new double[MAX];
	private double[] distancesDes2Can = new double[MAX];
	
	
	private double thredis = 2; // In kilometer; 
	private int id;
	private int type;
	private int travelPurpose;
	private int income;
	
	
	public DefaultAgent() {
		this.id = uniqueID++;
		double generator1 = Math.random();
		if (generator1 > 0 && generator1 < p1){
			type = 1; // Workplace
			travelPurpose = 5;
		} else if (generator1 > p1 && generator1 < (p1 + p2)){
			type = 2; //Shopping center
			travelPurpose = 0;
		} else if (generator1 > (p1 + p2) && generator1 < (p1 + p2 + p3)){
			type = 3; // Supermarket
			travelPurpose = 0;
		} else if (generator1 > (p1 + p2 + p3) && generator1 < (p1 + p2 + p3 + p4)){
			type = 4; // Restaurant
			travelPurpose = 0;
		} else {
			type = 5;
			travelPurpose = -5;
		}
		
		Random generator2 = new Random(new Date().getTime());
		income = generator2.nextInt(9) + 1;
		
		
		
	}

	@Override
	public void step() throws Exception {
		
		LOGGER.log(Level.FINE, "Agent " + this.id + " is stepping.");
		
			
		switch (type) {
		case 1: {
			
			if (this.route == null) {
				this.goingHome = false;
				Workplace w = ContextManager.workplaceContext.getRandomObject();
				
				this.route = new Route(this, ContextManager.workplaceProjection.getGeometry(w).getCentroid().getCoordinate(), w);
				this.origin = ContextManager.getAgentGeometry(this).getCoordinate();
				this.destination = ContextManager.workplaceProjection.getGeometry(w).getCentroid().getCoordinate();
				
				

				
			}
			
			if (!this.route.atDestination()) {
				this.route.travel();
				
				/**if (! disDone) {
					
					disDone = true;
					Iterable<Candidate1> iter_can1 = ContextManager.candidate1Context.getObjects(Candidate1.class);
					Iterator<Candidate1> iter = iter_can1.iterator();
					routedisogn = this.route.getShortestPathLength();

					int i = 0;
					while (iter.hasNext()) {
						
						Candidate1 can = iter.next();
						Coordinate transfer = ContextManager.candidate1Projection.getGeometry(can).getCentroid().getCoordinate();
						this.routedev1 = new Route(this.origin, transfer);
						this.routedev2 = new Route(transfer, this.destination);
						this.routedisdev1 = routedev1.myGetDistance();
						this.routedisdev2 = routedev2.myGetDistance();
						distances[i] = routedisdev1 + routedisdev2 - routedisogn; 
						i++;
						
					}
					
					
					
					
				}*/
				} else {
				
				if (this.goingHome) {
					this.goingHome = false;
					Residential R = ContextManager.residentialContext.getRandomObject();
					this.route = new Route(this, R.getCoords(), R);
				} 
				}
			
			
			/**Workplace w = ContextManager.workplaceContext.getRandomObject();
			//this.route = new Route(this, w.getCoords(), w);
			//this.route.travelTo(); //Need define this method by myself
			com.vividsolutions.jts.geom.Point desti = ContextManager.workplaceProjection.getGeometry(w).getCentroid();
			Coordinate co = desti.getCoordinate();
			double ran1 = Math.random() - 0.5;
			double ran2 = Math.random() - 0.5;
			co.x += ran1 * scale;
			co.y += ran2 * scale;
			ContextManager.moveAgent(this, desti);
			//System.out.print("Move finished!!");
			LOGGER.log(Level.FINE, this.toString() + "created new route to workplace" + w.toString());
			reachDestination = true;
			break;*/
				}
		
		case 2: {
			
			if (this.route == null) {
				this.goingHome = false;
				Shoppingcenter s = ContextManager.shoppingcenterContext.getRandomObject();

				
				this.route = new Route(this, ContextManager.shoppingcenterProjection.getGeometry(s).getCentroid().getCoordinate(), s);
				this.origin = ContextManager.getAgentGeometry(this).getCoordinate();
				this.destination = ContextManager.shoppingcenterProjection.getGeometry(s).getCentroid().getCoordinate();
				
			}
			
			if (!this.route.atDestination()) {
				this.route.travel();
				/**if (! disDone) {
					
					disDone = true;
					Iterable<Candidate1> iter_can1 = ContextManager.candidate1Context.getObjects(Candidate1.class);
					Iterator<Candidate1> iter = iter_can1.iterator();
					routedisogn = this.route.getShortestPathLength();

					int i = 0;
					while (iter.hasNext()) {
						
						Candidate1 can = iter.next();
						Coordinate transfer = ContextManager.candidate1Projection.getGeometry(can).getCentroid().getCoordinate();
						this.routedev1 = new Route(this.origin, transfer);
						this.routedev2 = new Route(transfer, this.destination);
						this.routedisdev1 = routedev1.myGetDistance();
						this.routedisdev2 = routedev2.myGetDistance();
						distances[i] = routedisdev1 + routedisdev2 - routedisogn; 
						i++;
						
					}
				}*/
				
				
				} else {
				
				if (this.goingHome) {
					this.goingHome = false;
					Residential R = ContextManager.residentialContext.getRandomObject();
					this.route = new Route(this, R.getCoords(), R);
				}
			}
			
				/**
			Shoppingcenter s = ContextManager.shoppingcenterContext.getRandomObject();
			//this.route = new Route(this, s.getCoords(), s);
			//this.route.travelTo();
			com.vividsolutions.jts.geom.Point desti = ContextManager.shoppingcenterProjection.getGeometry(s).getCentroid();
			Coordinate co = desti.getCoordinate();
			double ran1 = Math.random() - 0.5;
			double ran2 = Math.random() - 0.5;
			co.x += ran1 * scale;
			co.y += ran2 * scale;
			ContextManager.moveAgent(this, desti);
			//System.out.print("Move finished!!");
			LOGGER.log(Level.FINE, this.toString() + "created new route to shopping center" + s.toString());
			reachDestination = true;*/
			break;
			
			
				}
		case 3: {
			
			if (this.route == null) {
				this.goingHome = false;
				Supermarket S = ContextManager.supermarketContext.getRandomObject();
				
				this.route = new Route(this, ContextManager.supermarketProjection.getGeometry(S).getCentroid().getCoordinate(), S);
				this.origin = ContextManager.getAgentGeometry(this).getCoordinate();
				this.destination = ContextManager.supermarketProjection.getGeometry(S).getCentroid().getCoordinate();
			}
			
			if (!this.route.atDestination()) {
				this.route.travel();
				/**if (! disDone) {
					
					disDone = true;
					Iterable<Candidate1> iter_can1 = ContextManager.candidate1Context.getObjects(Candidate1.class);
					Iterator<Candidate1> iter = iter_can1.iterator();
					routedisogn = this.route.getShortestPathLength();

					int i = 0;
					while (iter.hasNext()) {
						
						Candidate1 can = iter.next();
						Coordinate transfer = ContextManager.candidate1Projection.getGeometry(can).getCentroid().getCoordinate();
						this.routedev1 = new Route(this.origin, transfer);
						this.routedev2 = new Route(transfer, this.destination);
						this.routedisdev1 = routedev1.myGetDistance();
						this.routedisdev2 = routedev2.myGetDistance();
						distances[i] = routedisdev1 + routedisdev2 - routedisogn; 
						i++;
						
					}
				}*/
				} else {
				
				if (this.goingHome) {
					this.goingHome = false;
					Residential R = ContextManager.residentialContext.getRandomObject();
					this.route = new Route(this, R.getCoords(), R);
				} 
				}
		}
		
		case 4: {
			
			if (this.route == null) {
				this.goingHome = false;
				Restaurant r = ContextManager.restaurantContext.getRandomObject();
				
				this.route = new Route(this, ContextManager.restaurantProjection.getGeometry(r).getCentroid().getCoordinate(), r);
				this.origin = ContextManager.getAgentGeometry(this).getCoordinate();
				this.destination = ContextManager.restaurantProjection.getGeometry(r).getCentroid().getCoordinate();
				
			}
			
			if (!this.route.atDestination()) {
				this.route.travel();
				/**if (! disDone) {
					
					disDone = true;
					Iterable<Candidate1> iter_can1 = ContextManager.candidate1Context.getObjects(Candidate1.class);
					Iterator<Candidate1> iter = iter_can1.iterator();
					routedisogn = this.route.getShortestPathLength();

					int i = 0;
					while (iter.hasNext()) {
						
						Candidate1 can = iter.next();
						Coordinate transfer = ContextManager.candidate1Projection.getGeometry(can).getCentroid().getCoordinate();
						this.routedev1 = new Route(this.origin, transfer);
						this.routedev2 = new Route(transfer, this.destination);
						this.routedisdev1 = routedev1.myGetDistance();
						this.routedisdev2 = routedev2.myGetDistance();
						distances[i] = routedisdev1 + routedisdev2 - routedisogn; 
						i++;
						
					}
				}*/
				} else {
				
				if (this.goingHome) {
					this.goingHome = false;
					Residential R = ContextManager.residentialContext.getRandomObject();
					this.route = new Route(this, R.getCoords(), R);
				} 
				}
			
			/**Restaurant r = ContextManager.restaurantContext.getRandomObject();
			//this.route = new Route(this, r.getCoords(), r);
			//this.route.travelTo();
			com.vividsolutions.jts.geom.Point desti = ContextManager.restaurantProjection.getGeometry(r).getCentroid();
			Coordinate co = desti.getCoordinate();
			double ran1 = Math.random() - 0.5;
			double ran2 = Math.random() - 0.5;
			co.x += ran1 * scale;
			co.y += ran2 * scale;
			ContextManager.moveAgent(this, desti);
			//System.out.print("Move finished!!");
			LOGGER.log(Level.FINE, this.toString() + "created new route to restaurant" + r.toString());
			reachDestination = true;*/
			break;
			
			
				}
		
		case 5: {
			
			if (this.route == null) {
				this.goingHome = false;
				Residential R = ContextManager.residentialContext.getRandomObject();
				while (R == home) {
					R = ContextManager.residentialContext.getRandomObject();
				}
				
				this.route = new Route(this, ContextManager.residentialProjection.getGeometry(R).getCentroid().getCoordinate(), R);
				this.origin = ContextManager.getAgentGeometry(this).getCoordinate();
				this.destination = ContextManager.residentialProjection.getGeometry(R).getCentroid().getCoordinate();
			}
			
			if (!this.route.atDestination()) {
				this.route.travel();
				/**if (! disDone) {
					
					disDone = true;
					Iterable<Candidate1> iter_can1 = ContextManager.candidate1Context.getObjects(Candidate1.class);
					Iterator<Candidate1> iter = iter_can1.iterator();
					routedisogn = this.route.getShortestPathLength();

					int i = 0;
					while (iter.hasNext()) {
						
						Candidate1 can = iter.next();
						Coordinate transfer = ContextManager.candidate1Projection.getGeometry(can).getCentroid().getCoordinate();
						this.routedev1 = new Route(this.origin, transfer);
						this.routedev2 = new Route(transfer, this.destination);
						this.routedisdev1 = routedev1.myGetDistance();
						this.routedisdev2 = routedev2.myGetDistance();
						distances[i] = routedisdev1 + routedisdev2 - routedisogn; 
						i++;
						
					}
				}*/
				} else {
				
				if (this.goingHome) {
					this.goingHome = false;
					Residential R = ContextManager.residentialContext.getRandomObject();
					this.route = new Route(this, R.getCoords(), R);
				} 
				/**else {
					this.goingHome = true;
					this.route = new Route(this, this.home.getCoords(), this.home);*/
					
				
			

			}
			break;
		}
		}

	}

/**
		if (this.route == null) {
			this.goingHome = false; // Must be leaving home
			// Choose a new building to go to
			Building b = ContextManager.buildingContext.getRandomObject();
			this.route = new Route(this, b.getCoords(), b);
			LOGGER.log(Level.FINE, this.toString() + " created new route to " + b.toString());
		}
		if (!this.route.atDestination()) {
			this.route.travel();
			LOGGER.log(Level.FINE, this.toString() + " travelling to " + this.route.getDestinationBuilding().toString());
		} else {
			// Have reached destination, now either go home or onto another building
			if (this.goingHome) {
				this.goingHome = false;
				Building b = ContextManager.buildingContext.getRandomObject();
				this.route = new Route(this, b.getCoords(), b);
				LOGGER.log(Level.FINE, this.toString() + " reached home, now going to " + b.toString());
			} else {
				LOGGER.log(Level.FINE, this.toString() + " reached " + this.route.getDestinationBuilding().toString()
						+ ", now going home");
				this.goingHome = true;
				this.route = new Route(this, this.home.getCoords(), this.home);
			}

		}

	} // step()
	*/

	/**
	 * There will be no inter-agent communication so these agents can be executed simulataneously in separate threads.
	 */
	@Override
	public final boolean isThreadable() {
		return true;
	}

	@Override
	public void setHome(Residential home) {
		this.home = home;
	}

	@Override
	public Residential getHome() {
		return this.home;
	}

	@Override
	public <T> void addToMemory(List<T> objects, Class<T> clazz) {
	}

	@Override
	public List<String> getTransportAvailable() {
		return null;
	}

	@Override
	public String toString() {
		return "Agent " + this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DefaultAgent))
			return false;
		DefaultAgent b = (DefaultAgent) obj;
		return this.id == b.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
	
	
	// Calculate the deviating distance 
	@Override
	public void calculateDis(){
		
		Iterable<Candidate1> iter_can1 = ContextManager.candidate1Context.getObjects(Candidate1.class);
		Iterator<Candidate1> iter = iter_can1.iterator();
		routedisogn = this.route.getShortestPathLength();

		int i = 0;
		while (iter.hasNext()) {
			
			Candidate1 can = iter.next();
			//System.out.print("iden is " + can.identifier + "\n" );
			Coordinate transfer = ContextManager.candidate1Projection.getGeometry(can).getCentroid().getCoordinate();
			this.routedev1 = new Route(this.origin, transfer);
			this.routedev2 = new Route(transfer, this.destination);
			this.routedisdev1 = routedev1.myGetDistance();
			this.routedisdev2 = routedev2.myGetDistance();
			double temp = (routedisdev1 + routedisdev2 - routedisogn) * 10; 
			if (temp > 0) {
				distances[i] = temp;
			} else {
				distances[i] = 0;
			}
			//System.out.print("distance is " + distances[i] + "\n");
			i++;
		}
	}
	
	// Calculate the direct distance from the agent to the candidate locations
	@Override
	public void calDisDes2Can() {
		
		DefaultEllipsoid e = DefaultEllipsoid.WGS84;
		Iterable<Candidate1> iter_can1 = ContextManager.candidate1Context.getObjects(Candidate1.class);
		Iterator<Candidate1> iter = iter_can1.iterator();
		int i = 0;
		while (iter.hasNext()) {
			
			Candidate1 can = iter.next();
			Coordinate can_coord = ContextManager.candidate1Projection.getGeometry(can).getCentroid().getCoordinate();
			distancesDes2Can[i] = e.orthodromicDistance(this.destination.x, this.destination.y, can_coord.x, can_coord.y) / 1000;
			//System.out.print("dis is " + distancesDes2Can[i] + "\n");
			i++;
			
			
		}
		
		
	}
	
	
	@Override
	public double returnDis(String id) {
		
		int iden = Integer.parseInt(id) - 1;
		//System.out.print("iden is " + iden + "\n");
		return distances[iden];
	}	
	
	@Override
	public int getTralPurp() {
		return travelPurpose;
	}

	@Override
	public int getIncome() {
		return income;
	}
	
	
	// Check if the charging station is near this agent's destination
	@Override
	public int isDes(String id) {
		
		if (this.type == 5)
			return 1;
		
		int iden = Integer.parseInt(id) - 1;
		//System.out.print("idn is " + iden + "\n");
		if (distancesDes2Can[iden] < thredis)
			return 1;
		else
			return 0;
		
		
	}
	
}
