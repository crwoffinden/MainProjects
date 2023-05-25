package PlayoffChances;

enum won {Win, Loss, Tie}

public class TeamGame {
    private Team opponent;
    private int scored;
    private int allowed;
    private won result = null;
    private boolean projection;
    
    public TeamGame(Team opp) {
        opponent = opp;
    }

    public Team getOpponent() {
        return opponent;
    }

    public int getScored() {
        return scored;
    }

    public int getAllowed() {
        return allowed;
    }

    public won getResult() {
        return result;
    }

    public void setScores(int pointsScored, int pointsAllowed) {
        scored = pointsScored;
        allowed = pointsAllowed;
        if (pointsScored > pointsAllowed) result = won.Win;
        else if (pointsScored < pointsAllowed) result = won.Loss;
        else result = won.Tie;
    }

    public void setProjection(boolean winProj) {
        projection = winProj;
    }

    public boolean getProjection() {
        return projection;
    }
}
