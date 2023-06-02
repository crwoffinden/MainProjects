package PlayoffChances;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class League {
    private Conference[] conferences = new Conference[2];

    private Set<Game> games = new HashSet<Game>(0);

    public void initialize(File schedule) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(schedule));
        while (in.hasNext()) {
            Team awayTeam = null;
            String awayName = in.next();
            for (int i = 0; i < conferences.length; ++i) {
                for (int j = 0; j < conferences[i].getDivisions().length; ++j) {
                    for (int k = 0; k < conferences[i].getDivisions()[j].getTeams().length; ++k) {
                        if (conferences[i].getDivisions()[j].getTeams()[k].getName() == awayName) {
                            awayTeam = conferences[i].getDivisions()[j].getTeams()[k];
                            i = conferences.length;
                            j = conferences[i].getDivisions().length;
                            k = conferences[i].getDivisions()[j].getTeams().length;
                        }
                    }
                }
            }
            int awayScore;
            if (in.hasNextInt()) awayScore = in.nextInt();
            else awayScore = -1;
            Team homeTeam = null;
            String homeName = in.next();
            for (int i = 0; i < conferences.length; ++i) {
                for (int j = 0; j < conferences[i].getDivisions().length; ++j) {
                    for (int k = 0; k < conferences[i].getDivisions()[j].getTeams().length; ++k) {
                        if (conferences[i].getDivisions()[j].getTeams()[k].getName() == homeName) {
                            awayTeam = conferences[i].getDivisions()[j].getTeams()[k];
                            i = conferences.length;
                            j = conferences[i].getDivisions().length;
                            k = conferences[i].getDivisions()[j].getTeams().length;
                        }
                    }
                }
            }
            int homeScore;
            if (in.hasNextInt()) homeScore = in.nextInt();
            else homeScore = -1;
            Game currGame = new Game(homeTeam, awayTeam, homeScore, awayScore);
            games.add(currGame);
        }   
    }

    public double project(double numProjections) { //FIXME figure out how to include calculation methods from other classes
        Game currGame = null;
        while (currGame == null && games.iterator().hasNext()) {
            Game nextGame = games.iterator().next();
            if (!nextGame.real()) {
                games.remove(nextGame);
                currGame = nextGame;
            }
        }
        if (currGame == null) return (numProjections + 1.0);
        currGame.project(false);
        numProjections = project(numProjections);
        currGame.project(true);
        numProjections = project(numProjections);
        games.add(currGame);
        return numProjections;
    }
}
