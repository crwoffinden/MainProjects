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

    @Override
    public boolean equals(Object obj) { //TODO add any new members
        if (this.getClass() != obj.getClass()) return false;
        if (this.headToHeadWins != ((Tiebreakers)obj).headToHeadWins) return false;
        if (this.headToHeadGames != ((Tiebreakers)obj).headToHeadGames) return false;
        if (this.divisionWins != ((Tiebreakers)obj).divisionWins) return false;
        if (this.divisionGames != ((Tiebreakers)obj).divisionGames) return false;
        if (this.commonGamesWon != ((Tiebreakers)obj).commonGamesWon) return false;
        if (this.commonGamesPlayed != ((Tiebreakers)obj).commonGamesPlayed) return false;
        if (this.conferenceWins != ((Tiebreakers)obj).conferenceWins) return false;
        if (this.conferenceGames != ((Tiebreakers)obj).conferenceGames) return false;
        return true;
    }
}
