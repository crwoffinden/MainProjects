package PlayoffChances;

import javax.swing.text.rtf.RTFEditorKit;

enum won {Win, Loss, Tie}

public class TeamGame {
    private Team opponent;
    private int scored;
    private int allowed;
    private won result = null;
    private boolean projection;
    
    public TeamGame(Team opp, int pointsScored, int pointsAllowed) {
        opponent = opp;
        scored = pointsScored;
        allowed = pointsAllowed;
        if (pointsScored != -1) {
            if (pointsScored > pointsAllowed) result = won.Win;
            else if (pointsScored < pointsAllowed) result = won.Loss;
            else result = won.Tie;
        }
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

    public void setProjection(boolean winProj) {
        projection = winProj;
    }

    public boolean getProjection() {
        return projection;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(this.getClass().equals(obj.getClass()))) return false;
        if (!(this.opponent.equals(((TeamGame)obj).opponent))) return false;
        if (this.scored != ((TeamGame)obj).scored) return false;
        if (this.allowed != ((TeamGame)obj).allowed) return false;
        if (this.result != ((TeamGame)obj).result) return false;
        if (this.projection != ((TeamGame)obj).projection) return false;
        return true;
    }

    public boolean teamEquals(Object obj) {
        if (!(this.getClass().equals(obj.getClass()))) return false;
        if (this.scored != ((TeamGame)obj).scored) return false;
        if (this.allowed != ((TeamGame)obj).allowed) return false;
        if (this.result != ((TeamGame)obj).result) return false;
        if (this.projection != ((TeamGame)obj).projection) return false;
        return true;
    }
}
