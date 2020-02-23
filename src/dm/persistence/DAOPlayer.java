package dm.persistence;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import dm.model.PerformanceLastSeason;
import dm.utility.Utils;

public class DAOPlayer implements IDAOPlayer {
	
	@Override
	public List<PerformanceLastSeason> retrieveOverallStatisticsLastSeason() throws DAOException {
		MongoClient mongoClient = null;
		List<PerformanceLastSeason> d_performances = new ArrayList<>();
		try {
			mongoClient = Utils.getMongoClient();
			MongoDatabase mongoDatabase = mongoClient.getDatabase("footballDB");
			AggregateIterable<Document> result = mongoDatabase.getCollection("players").aggregate(
					Arrays.asList(new Document("$unwind", 
						    new Document("path", "$detailedPerformances")), 
						    new Document("$project", 
						    new Document("fullName", 1L)
						            .append("bornDate", 1L)
						            .append("marketValue", 1L)
						            .append("team", 1L)
						            .append("nation", 1L)
						            .append("league", 1L)
						            .append("role", 1L)
						            .append("seasonDate", 
						    new Document("$split", Arrays.asList("$detailedPerformances.season", " - ")))
						            .append("detailedPerformances", 1L)), 
						    new Document("$unwind", 
						    new Document("path", "$seasonDate")), 
						    new Document("$group", 
						    new Document("_id", 
						    new Document("name", "$fullName")
						                .append("season", "$seasonDate")
						                .append("bornDate", "$bornDate"))
						            .append("season", 
						    new Document("$first", "$seasonDate"))
						            .append("fullName", 
						    new Document("$first", "$fullName"))
						            .append("role", 
						    new Document("$first", "$role"))
						            .append("nation", 
						    new Document("$first", "$nation"))
						            .append("team", 
						    new Document("$first", "$team"))
						            .append("bornDate", 
						    new Document("$first", "$bornDate"))
						            .append("marketValue", 
						    new Document("$first", "$marketValue"))
						            .append("yellowCards", 
						    new Document("$sum", "$detailedPerformances.yellowCards"))
						            .append("doubleYellowCards", 
						    new Document("$sum", "$detailedPerformances.doubleYellowCards"))
						            .append("redCards", 
						    new Document("$sum", "$detailedPerformances.redCards"))
						            .append("minutesPlayed", 
						    new Document("$sum", "$detailedPerformances.minutesPlayed"))
						            .append("ownGoals", 
						    new Document("$sum", "$detailedPerformances.ownGoals"))
						            .append("goals", 
						    new Document("$sum", "$detailedPerformances.goals"))
						            .append("assists", 
						    new Document("$sum", "$detailedPerformances.assists"))
						            .append("penaltyGoals", 
						    new Document("$sum", "$detailedPerformances.penaltyGoals"))
						            .append("goalConceded", 
						    new Document("$sum", "$detailedPerformances.goalsConceded"))
						            .append("cleanSheets", 
						    new Document("$sum", "$detailedPerformances.cleanSheets"))
						            .append("calls", 
						    new Document("$sum", "$detailedPerformances.calls"))
						            .append("presences", 
						    new Document("$sum", "$detailedPerformances.presences"))), 
						    new Document("$match", 
						    new Document("season", "19/20")))
					
//					Arrays.asList(new Document("$unwind", 
//						    new Document("path", "$detailedPerformances")), 
//						    new Document("$project", 
//						    new Document("fullName", 1L)
//						            .append("bornDate", 1L)
//						            .append("nation", 1L)
//						            .append("league", 1L)
//						            .append("role", 1L)
//						            .append("seasonDate", 
//						    new Document("$split", Arrays.asList("$detailedPerformances.season", " - ")))
//						            .append("detailedPerformances", 1L)), 
//						    new Document("$unwind", 
//						    new Document("path", "$seasonDate")), 
//						    new Document("$group", 
//						    new Document("_id", 
//						    new Document("name", "$fullName")
//						                .append("season", "$seasonDate")
//						                .append("bornDate", "$bornDate"))
//						            .append("season", 
//						    new Document("$first", "$seasonDate"))
//						            .append("fullName", 
//						    new Document("$first", "$fullName"))
//						            .append("role", 
//						    new Document("$first", "$role"))
//						            .append("team", 
//						    new Document("$first", "$detailedPerformances.team"))
//						            .append("nation", 
//						    new Document("$first", "$nation"))
//						            .append("bornDate", 
//						    new Document("$first", "$bornDate"))
//						            .append("yellowCards", 
//						    new Document("$sum", "$detailedPerformances.yellowCards"))
//						            .append("doubleYellowCards", 
//						    new Document("$sum", "$detailedPerformances.doubleYellowCards"))
//						            .append("redCards", 
//						    new Document("$sum", "$detailedPerformances.redCards"))
//						            .append("minutesPlayed", 
//						    new Document("$sum", "$detailedPerformances.minutesPlayed"))
//						            .append("ownGoals", 
//						    new Document("$sum", "$detailedPerformances.ownGoals"))
//						            .append("goals", 
//						    new Document("$sum", "$detailedPerformances.goals"))
//						            .append("assists", 
//						    new Document("$sum", "$detailedPerformances.assists"))
//						            .append("penaltyGoals", 
//						    new Document("$sum", "$detailedPerformances.penaltyGoals"))
//						            .append("goalConceded", 
//						    new Document("$sum", "$detailedPerformances.goalsConceded"))
//						            .append("cleanSheets", 
//						    new Document("$sum", "$detailedPerformances.cleanSheets"))
//						            .append("calls", 
//						    new Document("$sum", "$detailedPerformances.calls"))
//						            .append("presences", 
//						    new Document("$sum", "$detailedPerformances.presences"))), 
//						    new Document("$match", 
//						    new Document("season", "18/19")))
					).allowDiskUse(true);
			MongoCursor<Document> cursor = result.iterator();
			int i = 0;
			while(cursor.hasNext()) {
				i++;
				Document document = cursor.next();
				System.out.println("Player n." + i);
//				double marketValue = retrieveAvgMarketValueFromPlayer(document.getString("fullName"));
//				BigDecimal bd = new BigDecimal(marketValue);
//				document.put("marketValue", bd.toBigInteger().toString());
				Date bornDate = (Date) document.get("bornDate");
				if(bornDate != null) {
					String dateString = new SimpleDateFormat("dd/MM/yyyy").format(bornDate);
					document.put("bornDate", dateString);
				}
				PerformanceLastSeason performance = PerformanceLastSeason.teamFromJson(document.toJson().replaceAll("'", " "));
				d_performances.add(performance);
			}
		} catch(MongoException me) {
			throw new DAOException(me);
		} finally {
			if(mongoClient != null) {
				mongoClient.close();
			}
		}
		return d_performances;
	}
	
//	@Override
//	public double retrieveAvgMarketValueFromPlayer(String name) throws DAOException {
//		MongoClient mongoClient = null;
//		double avgMV = 0.0;
//		try {
//			mongoClient = Utils.getMongoClient();
//			MongoDatabase mongoDatabase = mongoClient.getDatabase("footballDB");
//			MongoCursor<Document> cursor = mongoDatabase.getCollection("players").aggregate(
////					Arrays.asList(new Document("$unwind", 
////						    new Document("path", "$marketValueHistory")), 
////						    new Document("$project", 
////						    new Document("fullName", 1L)
////						            .append("team", 1L)
////						            .append("seasonDate", 
////						    new Document("$split", Arrays.asList("$marketValueHistory.dateString", "/")))
////						            .append("marketValueHistory", 1L)), 
////						    new Document("$unwind", 
////						    new Document("path", "$seasonDate")), 
////						    new Document("$match", 
////						    new Document("$or", Arrays.asList(new Document("seasonDate", "2018"), 
////						                new Document("seasonDate", "2019")))
////						            .append("fullName", name)), 
////						    new Document("$group", 
////						    new Document("_id", 
////						    new Document("name", "$fullName")
////						                .append("seasonDateMW", "$seasonDateMW")
////						                .append("team", "$marketValueHistory.team"))
////						            .append("name", 
////						    new Document("$first", "$fullName"))
////						            .append("marketValue", 
////						    new Document("$avg", "$marketValueHistory.marketValue"))), 
////						    new Document("$match", 
////						    new Document("_id.team", team)))
//					
//					Arrays.asList(new Document("$unwind", 
//						    new Document("path", "$marketValueHistory")), 
//						    new Document("$project", 
//						    new Document("fullName", 1L)
//						            .append("team", 1L)
//						            .append("seasonDate", 
//						    new Document("$split", Arrays.asList("$marketValueHistory.dateString", "/")))
//						            .append("marketValueHistory", 1L)), 
//						    new Document("$unwind", 
//						    new Document("path", "$seasonDate")), 
//						    new Document("$match", 
//						    new Document("$or", Arrays.asList(new Document("seasonDate", "2018"), 
//						                new Document("seasonDate", "2019")))
//						            .append("fullName", name)), 
//						    new Document("$group", 
//						    new Document("_id", 
//						    new Document("name", "$fullName")
//						                .append("seasonDateMW", "$seasonDateMW"))
//						            .append("name", 
//						    new Document("$first", "$fullName"))
//						            .append("marketValue", 
//						    new Document("$avg", "$marketValueHistory.marketValue"))))
//					).iterator();
//			while(cursor.hasNext()) {
//				Document document = cursor.next();
//				avgMV = document.getDouble("marketValue");
//			}
//		} catch(MongoException me) {
//			throw new DAOException(me);
//		} finally {
//			if(mongoClient != null) {
//				mongoClient.close();
//			}
//		}
//		return avgMV;
//	}

}
