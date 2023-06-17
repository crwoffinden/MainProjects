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

    public League(Conference conference1, Conference conference2) {
        conferences[0] = conference1;
        conferences[1] = conference2;
    }

    public Conference[] getConferences() {
        return conferences;
    }

    public void initialize(String schedule) throws FileNotFoundException {
        String pathname = "\\Users\\crwof\\MainProjects\\PlayoffChances\\" + schedule;
        Scanner in = new Scanner(new FileReader(new File(pathname)));
        while (in.hasNext()) {
            Team awayTeam = null;
            String awayName = in.next();
            for (int i = 0; i < conferences.length; ++i) {
                for (int j = 0; j < conferences[i].getDivisions().length; ++j) {
                    for (int k = 0; k < conferences[i].getDivisions()[j].getTeams().length; ++k) {
                        if (conferences[i].getDivisions()[j].getTeams()[k].getName().equals(awayName)) {
                            awayTeam = conferences[i].getDivisions()[j].getTeams()[k];
                            break;
                        }
                    }
                    if (awayTeam != null) break;
                }
                if (awayTeam != null) break;
            }
            int awayScore;
            if (in.hasNextInt()) awayScore = in.nextInt();
            else awayScore = -1;
            //FIXME potential buffer?
            Team homeTeam = null;
            String homeName = in.next();
            for (int i = 0; i < conferences.length; ++i) {
                for (int j = 0; j < conferences[i].getDivisions().length; ++j) {
                    for (int k = 0; k < conferences[i].getDivisions()[j].getTeams().length; ++k) {
                        if (conferences[i].getDivisions()[j].getTeams()[k].getName().equals(homeName)) {
                            homeTeam = conferences[i].getDivisions()[j].getTeams()[k];
                            break;
                        }
                    }
                    if (homeTeam != null) break;
                }
                if (homeTeam != null) break;
            }
            int homeScore;
            if (in.hasNextInt()) homeScore = in.nextInt();
            else homeScore = -1;
            Game currGame = new Game(homeTeam, awayTeam, homeScore, awayScore);
            games.add(currGame);
        }   
    }

    public double project(double numProjections) {
        Game currGame = null;
        while (currGame == null && games.iterator().hasNext()) {
            Game nextGame = games.iterator().next();
            games.remove(nextGame);
            if (!nextGame.real()) {    
                currGame = nextGame;
            }
        }
        if (currGame == null) {
            runProjections();
            return (numProjections + 1.0);
        } 
        currGame.project(false);
        numProjections = project(numProjections);
        currGame.project(true);
        numProjections = project(numProjections);
        games.add(currGame);
        return numProjections;
    }

    public void runProjections() {
        for (int i = 0; i < conferences.length; ++i) {
            for (int j = 0; j < conferences[i].getDivisions().length; ++j) {
                for (int k = 0; k < conferences[i].getDivisions()[j].getTeams().length; ++k) {
                    conferences[i].getDivisions()[j].getTeams()[k].reset();
                    conferences[i].getDivisions()[j].getTeams()[k].projectGames();
                }
            }
            conferences[i].projectPlayoffs();
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < conferences.length; ++i) s.append(conferences[i].toString());
        return s.toString();
    }
}
