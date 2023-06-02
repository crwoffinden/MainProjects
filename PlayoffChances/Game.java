package PlayoffChances;

public class Game {
    private Team homeTeam;
    private Team awayTeam;
    private int homeScore;
    private int awayScore;
    private int homeIndex;
    private int awayIndex;

    public Game(Team homeTeam, Team awayTeam, int homeScore, int awayScore) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        homeIndex = homeTeam.addGame(awayTeam, homeScore, awayScore);
        awayIndex = awayTeam.addGame(homeTeam, awayScore, homeScore);
    }

    public boolean real() {
        return (homeScore != -1);
    }

    public void project(boolean homeWin) {
        homeTeam.getGames()[homeIndex].setProjection(homeWin);
        awayTeam.getGames()[awayIndex].setProjection(!homeWin);
    }
}
