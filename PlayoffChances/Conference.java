package PlayoffChances;

public class Conference {
    private Division[] divisions = new Division[4];

    public Conference(Division division1, Division division2, Division division3, Division division4) {
        divisions[0] = division1;
        divisions[1] = division2;
        divisions[2] = division3;
        divisions[3] = division4;
        for (int i = 0; i < divisions.length; ++i) divisions[i].setConference(this);
    }
    
    public Division[] getDivisions() {
        return divisions;
    }

    public void projectPlayoffs() { //TODO possibly adjust for chances of winning division/conference
        for (int i = 0; i < divisions.length; ++i) {
            divisions[i].rank(false);
            divisions[i].getTeams()[0].makesPlayoffs();
        }
        int eastIndex = 1;
        int northIndex = 1;
        int southIndex = 1;
        int westIndex = 1;
        for (int i = 0; i < 3; ++i) {
            double highestWinPct = 0.0;
            int numTeams = 0;
            Team[] teams = new Team[4];
            teams[0] = divisions[0].getTeams()[eastIndex];
            teams[1] = divisions[1].getTeams()[northIndex];
            teams[2] = divisions[2].getTeams()[southIndex];
            teams[3] = divisions[3].getTeams()[westIndex];
            for (int j = 0; j < teams.length; ++j) {
                if (teams[j].getProjectedWinPct() > highestWinPct) {
                    highestWinPct = teams[j].getProjectedWinPct();
                    numTeams = 1;
                }
                else if (teams[j].getProjectedWinPct() == highestWinPct) numTeams += 1;
            }
            int index = 0;
            if (numTeams == 1) {
                while ((index < teams.length) && teams[index].getProjectedWinPct() != highestWinPct) ++index;
                teams[index].makesPlayoffs();
            }
            else {
                Team[] tiedTeams = new Team[numTeams];
                for (int j = 0; j < teams.length; ++j) {
                    if (teams[j].getProjectedWinPct() == highestWinPct) {
                        tiedTeams[(tiedTeams.length - numTeams)] = teams[j];
                        --numTeams;
                    }
                }
                Team playoffTeam = tiebreak(tiedTeams, false);
                while ((index < teams.length) && (teams[index] != playoffTeam)) ++index;
                playoffTeam.makesPlayoffs();
            }
            switch(index) {
                case(0): eastIndex += 1;
                case(1): northIndex += 1;
                case(2): southIndex += 1;
                case(3): westIndex += 1;
            }
        }
    }

    public Team tiebreak(Team[] tiedTeams, boolean real) {
        Team leader = null;
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

                    for (int a = 0; a < divisions.length; ++a) {
                        for (int b = 0; b < divisions[a].getTeams().length; ++b) {
                            if (divisions[a].getTeams()[b] == opponent) {
                                teamTiebreakers[i].conferenceGame(wins);
                                a = divisions.length;
                                b = divisions[a].getTeams().length;
                            }
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
        
        if ((teamTiebreakers[0].getHeadToHeadGames() == (tiedTeams.length - 1)) && (teamTiebreakers[0].getHeadToHeadRecord() == 1.0)) leader = tiedTeams[0];
        else if ((teamTiebreakers[teamTiebreakers.length - 1].getHeadToHeadGames() == (tiedTeams.length - 1)) && (teamTiebreakers[teamTiebreakers.length - 1].getHeadToHeadRecord() == 0.0)) {
            Team[] newTie = new Team[tiedTeams.length - 1];
            for (int i = 0; i < newTie.length; ++i) newTie[i] = tiedTeams[i];
            leader = tiebreak(newTie, real);
        }
        else {
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
            int stillTied = 1;
            while ((stillTied < tiedTeams.length) && (teamTiebreakers[stillTied].getConferenceRecord() == teamTiebreakers[stillTied - 1].getConferenceRecord())) ++stillTied;
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
                if (teamTiebreakers[0].getCommonGamesPlayed() >= 4.0) {
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
        if (obj.getClass() != this.getClass()) return false;
        for (int i = 0; i < divisions.length; ++i) {
            if (this.divisions[i] != ((Conference)obj).divisions[i]) return false;
        }
        return true;
    }
}
