package PlayoffChances;

class Team {
    private String city;
    private String name;
    private TeamGame[] games = new TeamGame[17];
    private int realWins = 0;
    private int projectedWins = 0;
    private int realLosses = 0;
    private int projectedLosses = 0;
    private int ties = 0;
    private double realWinPct = 0.0;
    private double projectedWinPct = 0.0;
    private int projectedPlayoffAppearances = 0;

    public Team(String teamCity, String teamName) {
        city = teamCity;
        name = teamName;
    }

    public void addGame(Team opponent) {
        int index = 0;
        while (games[index] != null) ++ index;
        games[index] = new TeamGame(opponent);
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public TeamGame[] getGames() {
        return games;
    }

    public int getRealWins() {
        return realWins;
    }

    public int getRealLosses() {
        return realLosses;
    }

    public int getTies() {
        return ties;
    }

    public double getRealWinPct() {
        return realWinPct;
    }

    public double getProjectedWinPct() {
        return projectedWinPct;
    }

    public void reset() {
        projectedWins = realWins;
        projectedLosses = realLosses;
        projectedWinPct = (realWins + (double) (ties / 2)) / (realWins + realLosses + ties);
        projectedPlayoffAppearances = 0;
    }

    public void realGames() {
        int index = 0;
        while (games[index].getResult() != null) {
            if (games[index].getResult() == won.Win) realWins += 1;
            else if (games[index].getResult() == won.Loss) realLosses += 1;
            else if (games[index].getResult() == won.Tie) ties += 1;
            index += 1;
        }
        realWinPct = (realWins + (double) (ties / 2)) / (realWins + realLosses + ties);
        reset();
    }

    public void projectGames() {
        int index = realWins + realLosses + ties;
        while (index < 17) {
            if (games[index].getProjection()) projectedWins += 1;
            else projectedLosses += 1;
        }
        projectedWinPct = (projectedWins + (double) (ties / 2)) / (projectedWins + projectedLosses + ties);
    }

    public int getProjectedPlayoffAppearances() {
        return projectedPlayoffAppearances;
    }

    public void makesPlayoffs() {
        projectedPlayoffAppearances += 1;
    }
}