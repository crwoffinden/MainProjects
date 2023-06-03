package PlayoffChances;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static int main(String[] args) throws FileNotFoundException {
        Team buffaloBills = new Team("Buffalo", "Bills");
        Team miamiDolphins = new Team("Miami", "Dolphins");
        Team newEnglandPatriots = new Team("New England", "Patriots");
        Team newYorkJets = new Team("New York", "Jets");
        Division afcEast = new Division(buffaloBills, miamiDolphins, newEnglandPatriots, newYorkJets);

        Team baltimoreRavens = new Team("Baltimore", "Ravens");
        Team cincinnattiBengals = new Team("Cincinnatti", "Bengals");
        Team clevelandBrowns = new Team("Cleveland", "Browns");
        Team pittsburghSteelers = new Team("Pittsburgh", "Steelers");
        Division afcNorth = new Division(baltimoreRavens, cincinnattiBengals, clevelandBrowns, pittsburghSteelers);

        Team houstonTexans = new Team("Houston", "Texans");
        Team indianapolisColts = new Team("Indianapolis", "Colts");
        Team jacksonvilleJaguars = new Team("Jacksonville", "Jaguars");
        Team tennesseeTitans = new Team("Tennessee", "Titans");
        Division afcSouth = new Division(houstonTexans, indianapolisColts, jacksonvilleJaguars, tennesseeTitans);

        Team denverBroncos = new Team("Denver", "Broncos");
        Team kansasCityChiefs = new Team("Kansas City", "Chiefs");
        Team lasVegasRaiders = new Team("Las Vegas", "Raiders");
        Team losAngelesChargers = new Team("Los Angeles", "Chargers");
        Division afcWest = new Division(denverBroncos, kansasCityChiefs, lasVegasRaiders, losAngelesChargers);
        Conference afc = new Conference(afcEast, afcNorth, afcSouth, afcWest);
        
        Team dallasCowboys = new Team("Dallas", "Cowboys");
        Team newYorkGiants = new Team("New York", "Giants");
        Team philadelphiaEagles = new Team("Philadelphia", "Eagles");
        Team washingtonCommanders = new Team("Washington", "Commanders");
        Division nfcEast = new Division(dallasCowboys, newYorkGiants, philadelphiaEagles, washingtonCommanders);

        Team chicagoBears = new Team("Chicago", "Bears");
        Team detroitLions = new Team("Detroit", "Lions");
        Team greenBayPackers = new Team("Green Bay", "Packers");
        Team minnesotaVikings = new Team("Minnesota", "Vikings");
        Division nfcNorth = new Division(chicagoBears, detroitLions, greenBayPackers, minnesotaVikings);

        Team atlantaFalcons = new Team("Atlanta", "Falcons");
        Team carolinaPanthers = new Team("Carolina", "Panthers");
        Team newOrleansSaints = new Team("New Orleans", "Saints");
        Team TampaBayBuccaneers = new Team("Tampa Bay", "Buccaneers");
        Division nfcSouth = new Division(atlantaFalcons, carolinaPanthers, newOrleansSaints, TampaBayBuccaneers);

        Team arizonaCardinals = new Team("Arizona", "Cardinals");
        Team losAngelesRams = new Team("Los Angeles", "Rams");
        Team sanFrancisco49ers = new Team("San Francisco", "49ers");
        Team seattleSeahawks = new Team("Seattle", "Seahawks");
        Division nfcWest = new Division(arizonaCardinals, losAngelesRams, sanFrancisco49ers, seattleSeahawks);
        Conference nfc = new Conference(nfcEast, nfcNorth, nfcSouth, nfcWest);
        League nfl = new League(afc, nfc);

        String scheduleFileName = args[0];
        File schedule = new File(scheduleFileName);
        nfl.initialize(schedule);
        double numProjections = nfl.project(0);
        for (int i = 0; i < nfl.getConferences().length; ++i) {
            for (int j = 0; j < nfl.getConferences()[i].getDivisions().length; ++j) {
                for (int k = 0; k < nfl.getConferences()[i].getDivisions()[j].getTeams().length; ++k) {
                    nfl.getConferences()[i].getDivisions()[j].getTeams()[k].getPlayoffChances(numProjections);
                }
            }
        }

        return 0;
    }
}
