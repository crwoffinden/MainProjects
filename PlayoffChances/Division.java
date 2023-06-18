package PlayoffChances;

public class Division {
    private String name;
    private Team[] teams = new Team[4];
    private Conference conferenceOf;
    
    public Division(String divisionName, Team team1, Team team2, Team team3, Team team4) {
        name = divisionName;
        teams[0] = team1;
        teams[1] = team2;
        teams[2] = team3;
        teams[3] = team4;
    }

    public void setConference(Conference conference) {
        conferenceOf = conference;
    } 

    public Team[] getTeams() {
        return teams;
    }

    public void rank(boolean real) {
        double[] winPcts = new double[4];
        for (int i = 0; i < teams.length; ++i) {
            if (!real) winPcts[i] = teams[i].getProjectedWinPct();
            else winPcts[i] = teams[i].getRealWinPct();
            int j = i;
            while (j > 0 && winPcts[j] > winPcts[j - 1]) {
                Team temp = teams[j - 1];
                teams[j - 1] = teams[j];
                teams[j] = temp;
                double tempWinPct = winPcts[j - 1];
                winPcts[j - 1] = winPcts[j];
                winPcts[j] = tempWinPct;
                --j;
            }
        }
        int index = 0;
        while (index < teams.length) {
            int tied = 1;
            while (((index + tied) < teams.length) && (winPcts[index + tied] == winPcts[index + tied - 1])) ++tied;
            if (tied > 1) {
                Team[] tiedTeams = new Team[tied];
                for (int j = 0; j < tied; ++j) tiedTeams[j] = teams[index + j];
                Team leader = tiebreak(tiedTeams, real);
                int tempIndex = index;
                while ((tempIndex < teams.length) && (!(teams[tempIndex].equals(leader)))) ++tempIndex;
                teams[tempIndex] = teams[index];
                teams[index] = leader;
                double tempPct = winPcts[tempIndex];
                winPcts[tempIndex] = winPcts[index];
                winPcts[index] = tempPct;
            }
            ++index;
        }
    }

    public Team tiebreak(Team[] tiedTeams, boolean real) {
        Team leader = null;
        Tiebreakers[] teamTiebreakers = new Tiebreakers[tiedTeams.length];
        TeamGame[][] schedules = new TeamGame[tiedTeams.length][];
        for (int i = 0; i < tiedTeams.length; ++i) {
            teamTiebreakers[i] = new Tiebreakers();
            schedules[i] = tiedTeams[i].getGames();
        }
        for (int i = 0; i < tiedTeams.length; ++i) {
            for (int j = 0; j < schedules[i].length; ++j) {
                if ((schedules[i][j] == null) || ((schedules[i][j].getResult() == null) && real)) j = schedules[i].length;
                else {
                    double wins = 0.0;
                    if ((schedules[i][j].getResult() == won.Win) || (!real && schedules[i][j].getProjection())) wins = 1.0;
                    else if ((schedules[i][j].getResult() == won.Loss) || (!real && !schedules[i][j].getProjection())) wins = 0.0;
                    else if (schedules[i][j].getResult() == won.Tie) wins = 0.5;
                    Team opponent = schedules[i][j].getOpponent();
                    for (int w = 0; w < tiedTeams.length; ++w) {
                        if (opponent.equals(tiedTeams[w]))  {
                            teamTiebreakers[i].headToHeadGame(wins);
                            w = tiedTeams.length;
                        }
                    }
                    for (int x = 0; x < teams.length; ++x) {
                        if (opponent.equals(teams[x])) {
                            teamTiebreakers[i].divisionGame(wins);
                            x = teams.length;
                        }
                    }
                    boolean common = true;
                    for (int y = 0; y < tiedTeams.length; ++y) {
                        if ((y != i) && common) {
                            int game = 0;
                            while ((game < schedules.length) && (!(schedules[y][game].getOpponent().equals(opponent)))) ++game;
                            if (game == schedules.length) common = false;
                        }
                    }
                    if (common) {
                        teamTiebreakers[i].commonGame(wins);
                    }
                    for (int a = 0; a < conferenceOf.getDivisions().length; ++a) {
                        for (int b = 0; b < teams.length; ++b) {
                            if (conferenceOf.getDivisions()[a].getTeams()[b].equals(opponent)) {
                                teamTiebreakers[i].conferenceGame(wins);
                                a = conferenceOf.getDivisions().length;
                                b = teams.length;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 1; i < tiedTeams.length; ++i) {
            int j = i;
            while ((j > 0) && ((teamTiebreakers[j].getHeadToHeadRecord() > teamTiebreakers[j - 1].getHeadToHeadRecord()) || ((teamTiebreakers[j].getHeadToHeadRecord() == teamTiebreakers[j - 1].getHeadToHeadRecord()) && (teamTiebreakers[j].getHeadToHeadGames() > teamTiebreakers[j - 1].getHeadToHeadGames())))) {
                Team temp = tiedTeams[j - 1];
                tiedTeams[j - 1] = tiedTeams[j];
                tiedTeams[j] = temp;
                Tiebreakers tempTiebreakers = teamTiebreakers[j - 1];
                teamTiebreakers[j - 1] = teamTiebreakers[j];
                teamTiebreakers[j] = tempTiebreakers;
                --j;
            }
        }
        
        int stillTied = 1;
        while ((stillTied < tiedTeams.length) && (teamTiebreakers[stillTied].getHeadToHeadRecord() == teamTiebreakers[stillTied - 1].getHeadToHeadRecord())) ++stillTied;
        if (stillTied == 1) leader = tiedTeams[0];
        else if (stillTied < tiedTeams.length) {
            Team[] newTie = new Team[stillTied];
            for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[j];
            leader = tiebreak(tiedTeams, real);
        }
        else if (stillTied == tiedTeams.length) {
            for (int i = 1; i < tiedTeams.length; ++i) {
                int j = i;
                while ((j > 0) && (teamTiebreakers[j].getDivisionRecord() > teamTiebreakers[j - 1].getDivisionRecord())) {
                    Team temp = tiedTeams[j - 1];
                    tiedTeams[j - 1] = tiedTeams[j];
                    tiedTeams[j] = temp;
                    Tiebreakers tempTiebreakers = teamTiebreakers[j - 1];
                    teamTiebreakers[j - 1] = teamTiebreakers[j];
                    teamTiebreakers[j] = tempTiebreakers;
                    --j;
                }
            }
            stillTied = 1;
            while ((stillTied < tiedTeams.length) && (teamTiebreakers[stillTied].getDivisionRecord() == teamTiebreakers[stillTied - 1].getDivisionRecord())) ++stillTied;
            if (stillTied == 1) leader = tiedTeams[0];
            else if (stillTied < tiedTeams.length) {
                Team[] newTie = new Team[stillTied];
                for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[j];
                leader = tiebreak(tiedTeams, real);
            }
            else if (stillTied == tiedTeams.length) {
                for (int i = 1; i < tiedTeams.length; ++i) {
                    int j = i;
                    while ((j > 0) && (teamTiebreakers[j].getCommonGamesRecord() > teamTiebreakers[j - 1].getCommonGamesRecord())) {
                        Team temp = tiedTeams[j - 1];
                        tiedTeams[j - 1] = tiedTeams[j];
                        tiedTeams[j] = temp;
                        Tiebreakers tempTiebreakers = teamTiebreakers[j - 1];
                        teamTiebreakers[j - 1] = teamTiebreakers[j];
                        teamTiebreakers[j] = tempTiebreakers;
                        --j;
                    }
                }
                stillTied = 1;
                while ((stillTied < tiedTeams.length) && (teamTiebreakers[stillTied].getHeadToHeadRecord() == teamTiebreakers[stillTied - 1].getHeadToHeadRecord())) ++stillTied;
                if (stillTied == 1) leader = tiedTeams[0];
                else if (stillTied < tiedTeams.length) {
                    Team[] newTie = new Team[stillTied];
                    for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[j];
                    leader = tiebreak(tiedTeams, real);
                }
                else if (stillTied == tiedTeams.length) {
                    for (int i = 1; i < tiedTeams.length; ++i) {
                        int j = i;
                        while ((j > 0) && (teamTiebreakers[j].getConferenceRecord() > teamTiebreakers[j - 1].getConferenceRecord())) {
                            Team temp = tiedTeams[j - 1];
                            tiedTeams[j - 1] = tiedTeams[j];
                            tiedTeams[j] = temp;
                            Tiebreakers tempTiebreakers = teamTiebreakers[j - 1];
                            teamTiebreakers[j - 1] = teamTiebreakers[j];
                            teamTiebreakers[j] = tempTiebreakers;
                            --j;
                        }
                    }
                    stillTied = 1;
                    while ((stillTied < tiedTeams.length) && (teamTiebreakers[stillTied].getHeadToHeadRecord() == teamTiebreakers[stillTied - 1].getHeadToHeadRecord())) ++stillTied;
                    if (stillTied == 1) leader = tiedTeams[0];
                    else if (stillTied < tiedTeams.length) {
                        Team[] newTie = new Team[stillTied];
                        for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[j];
                        leader = tiebreak(tiedTeams, real);
                    }
                }
            }
        }
        return leader;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj.getClass().equals(this.getClass()))) return false;
        if (!(this.name.equals(((Division)obj).name))) return false;
        for (int i = 0; i < teams.length; ++i) {
            if (!(this.teams[i].equals(((Division)obj).teams[i]))) return false;
        }
        if (!(this.conferenceOf.equals(((Division)obj).conferenceOf))) return false;
        return true;
    }

    @Override
    public String toString() {
        rank(true);
        StringBuilder s = new StringBuilder();
        s.append(name); //FIXME figure out buffer
        s.append("W");
        s.append("L");
        s.append("T");
        s.append("PCT");
        s.append("Playoff %");
        s.append("Division %");
        s.append("Wild Card %");
        s.append("Conference %");
        s.append("\n");
        for (int i = 0; i < teams.length; ++i) {
            s.append(teams[i].toString());
        }
        return s.toString();
    }
}
