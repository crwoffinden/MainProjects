package PlayoffChances;

public class Division {
    private Team[] teams = new Team[4];
    private Conference conferenceOf;
    
    public Division(Team team1, Team team2, Team team3, Team team4, Conference conference) {
        teams[0] = team1;
        teams[1] = team2;
        teams[2] = team3;
        teams[3] = team4;
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
        for (int i = 0; i < (teams.length - 1); ++i) {
            int tied = 1;
            while (((i + tied) < teams.length) && winPcts[i + tied] == winPcts[i]) ++tied;
            if (tied > 1) {
                Team[] tiedTeams = new Team[tied];
                for (int j = 0; j < tied; ++j) tiedTeams[j] = teams[i + j];
                Team[] brokenTie = tiebreak(tiedTeams, real);
            }
        }
    }

    public Team[] tiebreak(Team[] tiedTeams, boolean real) { //FIXME set up tiebreakers struct and figure out the rest
        Team[] brokenTie = new Team[tiedTeams.length];
        Tiebreakers[] teamTiebreakers = new Tiebreakers[tiedTeams.length];
        TeamGame[][] schedules = new TeamGame[tiedTeams.length][];
        for (int i = 0; i < tiedTeams.length; ++i) schedules[i] = tiedTeams[i].getGames();
        for (int i = 0; i < tiedTeams.length; ++i) {
            for (int j = 0; j < schedules[i].length; ++j) {
                if ((schedules[i][j].getResult() == null) && real) j = schedules[i].length;
                else {
                    double wins = 0.0;
                    if ((schedules[i][j].getResult() == won.Win) || (!real && schedules[i][j].getProjection())) wins = 1.0;
                    else if ((schedules[i][j].getResult() == won.Loss) || (!real && !schedules[i][j].getProjection())) wins = 0.0;
                    else if (schedules[i][j].getResult() == won.Tie) wins = 0.5;
                    Team opponent = schedules[i][j].getOpponent();
                    for (int w = 0; w < tiedTeams.length; ++w) {
                        if (opponent == tiedTeams[w])  {
                            teamTiebreakers[i].headToHeadGame(wins);
                            w = tiedTeams.length;
                        }
                    }
                    for (int x = 0; x < teams.length; ++x) {
                        if (opponent == teams[x]) {
                            teamTiebreakers[i].divisionGame(wins);
                            x = teams.length;
                        }
                    }
                    boolean common = true;
                    for (int y = 0; y < tiedTeams.length; ++y) {
                        if ((y != i) && common) {
                            int game = 0;
                            while ((game < schedules.length) && (schedules[y][game].getOpponent() != opponent)) ++game;
                            if (game == schedules.length) common = false;
                        }
                    }
                    if (common) {
                        teamTiebreakers[i].commonGame(wins);
                    }
                    for (int a = 0; a < conferenceOf.getDivisions().length; ++a) {
                        for (int b = 0; b < teams.length; ++b) {
                            if (conferenceOf.getDivisions()[a].getTeams()[b] == opponent) {
                                teamTiebreakers[i].conferenceGame(wins);
                                a = conferenceOf.getDivisions().length;
                                b = teams.length;
                                //break;
                            }
                        }
                    }
                }
            }
        }
        for (int i = 1; i < tiedTeams.length; ++i) {
            int j = i;
            while ((j > 0) && (teamTiebreakers[j].getHeadToHeadRecord() > teamTiebreakers[j - 1].getHeadToHeadRecord())) {
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
        for (int i = 0; i < tiedTeams.length; i += stillTied) {
            stillTied = 1;
            while (teamTiebreakers[i + stillTied].getHeadToHeadRecord() == teamTiebreakers[i].getHeadToHeadRecord()) ++stillTied;
            if (stillTied == 1) brokenTie[i] = tiedTeams[i];
            else if (stillTied < tiedTeams.length) {
                Team[] newTie = new Team[stillTied];
                for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[i + j];
                Team[] brokenNewTie = tiebreak(newTie, real);
                for (int j = 0; j < stillTied; ++j) brokenTie[i + j] = brokenNewTie[j];
            }
        }

        if (stillTied == tiedTeams.length) {
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
            for (int i = 0; i < tiedTeams.length; i += stillTied) {
                stillTied = 1;
                while (teamTiebreakers[i + stillTied].getDivisionRecord() == teamTiebreakers[i].getDivisionRecord()) ++stillTied;
                if (stillTied == 1) brokenTie[i] = tiedTeams[i];
                else if (stillTied < tiedTeams.length) {
                    Team[] newTie = new Team[stillTied];
                    for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[i + j];
                    Team[] brokenNewTie = tiebreak(newTie, real);
                    for (int j = 0; j < stillTied; ++j) brokenTie[i + j] = brokenNewTie[j];
                }
            }
            if (stillTied == tiedTeams.length) {
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
                for (int i = 0; i < tiedTeams.length; i += stillTied) {
                    stillTied = 1;
                    while (teamTiebreakers[i + stillTied].getCommonGamesRecord() == teamTiebreakers[i].getCommonGamesRecord()) ++stillTied;
                    if (stillTied == 1) brokenTie[i] = tiedTeams[i];
                    else if (stillTied < tiedTeams.length) {
                        Team[] newTie = new Team[stillTied];
                        for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[i + j];
                        Team[] brokenNewTie = tiebreak(newTie, real);
                        for (int j = 0; j < stillTied; ++j) brokenTie[i + j] = brokenNewTie[j];
                    }
                }
                if (stillTied == tiedTeams.length) {
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
                    for (int i = 0; i < tiedTeams.length; i += stillTied) {
                        stillTied = 1;
                        while (teamTiebreakers[i + stillTied].getConferenceRecord() == teamTiebreakers[i].getConferenceRecord()) ++stillTied;
                        if (stillTied == 1) brokenTie[i] = tiedTeams[i];
                        else if (stillTied < tiedTeams.length) {
                            Team[] newTie = new Team[stillTied];
                            for (int j = 0; j < stillTied; ++j) newTie[j] = tiedTeams[i + j];
                            Team[] brokenNewTie = tiebreak(newTie, real);
                            for (int j = 0; j < stillTied; ++j) brokenTie[i + j] = brokenNewTie[j];
                        }
                    }
                }
            }
        }
        return brokenTie;
    }
}
