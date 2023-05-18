package PlayoffChances;

class Team {
    private String city;
    private String name;
    private TeamGame[] games;
    private int realWins;
    private int projectedWins;
    private int realLosses;
    private int projectedLosses;
    private int ties;
    private double realWinPct;
    private double projectedWinPct;
    private int projectedPlayoffAppearances;

    public String getName() {
        return name;
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
}