package PlayoffChances;

public class Tiebreakers {
    private double headToHeadWins = 0.0;
    private double headToHeadGames = 0.0;
    private double divisionWins = 0.0;
    private double divisionGames = 0.0;
    private double commonGamesWon = 0.0;
    private double commonGamesPlayed = 0.0;
    private double conferenceWins = 0.0;
    private double conferenceGames = 0.0;

    public void headToHeadGame(double wins) {
        headToHeadWins += wins;
        headToHeadGames += 1.0;
    }

    public void divisionGame(double wins) {
        divisionWins += wins;
        divisionGames += 1.0;
    }

    public void commonGame(double wins) {
        commonGamesWon += wins;
        commonGamesPlayed += 1.0;
    }

    public void conferenceGame(double wins) {
        conferenceWins += wins;
        conferenceGames += 1.0;
    }

    public double getHeadToHeadRecord() {
        return (headToHeadWins / headToHeadGames);
    }

    public double getDivisionRecord() {
        return (divisionWins / divisionGames);
    }

    public double getCommonGamesRecord() {
        return (commonGamesWon / commonGamesPlayed);
    }

    public double getConferenceRecord() {
        return (conferenceWins / conferenceGames);
    }
}
