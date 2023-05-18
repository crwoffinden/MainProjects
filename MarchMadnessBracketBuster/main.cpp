#include <iostream>
#include <iomanip>
#include <string>
#include <vector>
#include <fstream>
using namespace std;

void CompareTeamsSweetSixteen(vector<string> playingTeamsS16, vector<int> playingSeedsS16,
                               vector<string>& winningTeamsS16, vector<int>& winningSeedsS16, unsigned int index,
                               bool& upsetTracker) {
    winningTeamsS16.at(index / 2) = playingTeamsS16.at(index);
    winningSeedsS16.at(index / 2) = playingSeedsS16.at(index);
    if ((index % 2) == 0)   {
        if (winningSeedsS16.at(index / 2) > playingSeedsS16.at(index + 1)) {
            upsetTracker = true;
        }
        else {
            upsetTracker = false;
        }
    }
    else {
        if (winningSeedsS16.at(index / 2) > playingSeedsS16.at(index - 1)) {
            upsetTracker = true;
        }
        else {
            upsetTracker = false;
        }
    }
}

void CompareTeams(vector<string> playingTeams, vector<int> playingSeeds, vector<string>& winningTeams,
             vector<int>& winningSeeds, vector<string>& losingTeams, unsigned int index, bool& upsetTracker) {
    winningTeams.at(index / 2) = playingTeams.at(index);
    winningSeeds.at(index / 2) = playingSeeds.at(index);
    if ((index % 2) == 0) {
        losingTeams.at(index / 2) = playingTeams.at(index + 1);
        if (winningSeeds.at(index / 2) > playingSeeds.at(index + 1)) {
            upsetTracker = true;
        }
        else {
            upsetTracker = false;
        }
    }
    else {
        losingTeams.at(index / 2) = playingTeams.at(index - 1);
        if (winningSeeds.at(index / 2) > playingSeeds.at(index - 1)) {
            upsetTracker = true;
        }
        else {
            upsetTracker = false;
        }
    }
}

void CompareTeamsChampionship(vector<string> championshipTeams, vector<int> championshipSeeds, string& winningTeam,
                              string& losingTeam, unsigned int index, bool& upsetTracker) {
    winningTeam= championshipTeams.at(index);
    if ((index % 2) == 0) {
        losingTeam = championshipTeams.at(index + 1);
        if (championshipSeeds.at(index) > championshipSeeds.at(index + 1)) {
            upsetTracker = true;
        }
        else {
            upsetTracker = false;
        }
    }
    else {
        losingTeam = championshipTeams.at(index - 1);
        if (championshipSeeds.at(index) > championshipSeeds.at(index - 1)) {
            upsetTracker = true;
        }
        else {
            upsetTracker = false;
        }
    }
}

void AwardPoints(vector<int>& scenarioPoints, vector<string>& teamsChosen, string& winningTeam, bool isUpset,
                 unsigned int pointsAwarded) {
    string selectedTeam;
    unsigned int newIndex;
    unsigned int oldIndex;
    unsigned int i;
    unsigned int j;
    for (i = 0; i < teamsChosen.size(); ++i) {
        oldIndex = 0;
        for (j = 0; j < (32 / pointsAwarded); ++j) {
            if (j < 7) {
                newIndex = teamsChosen.at(i).find(',', oldIndex);
            }
            else {
                newIndex = teamsChosen.at(i).size();
            }
            selectedTeam = teamsChosen.at(i).substr(oldIndex, (newIndex - oldIndex));
            if (selectedTeam == winningTeam) {
                scenarioPoints.at(i) += pointsAwarded;
                if (isUpset) {
                    scenarioPoints.at(i) += 1;
                }
                break;
            }
            else {
                oldIndex = newIndex + 2;
            }
        }
    }

}

int main(int argc, char** argv) {
    if (argc < 2) {
        cerr << "Please provide output file.";
        return 1;
    }
    ofstream out(argv[1]);
    if (!out.is_open()) {
        cerr << "Unable to open " << argv[1] << " for output";
        return 2;
    }

    unsigned int numBrackets;
    unsigned int numTeams = 16;
    unsigned int x;
    unsigned int y;
    unsigned int z = 0;
    string tempString;
    bool upsetAlert = false;

    cout << "How many brackets? ";
    cin >> numBrackets;
    cout << endl;

    vector<string> userNames(numBrackets);
    int currPoints[numBrackets][numTeams];
    vector<int> newPoints(numBrackets);
    vector<string> remainingTeams(numBrackets);
    string chosenTeam;
    int highScore;
    double numWinners = 0.0;
    vector<double> numWins(numBrackets);
    double numWinsPerScenario[numBrackets][2][2][2][2][2][2][2][2];

    for (x = 0; x < numBrackets; ++x){
        for (z = 0; z < numTeams; ++z) {
            currPoints[x][z] = 0;
        }
    }
    z = 0;

    for (x = 0; x < numBrackets; ++x) {
        cout << "Enter player " << x + 1 << "'s name: ";
        cin >> userNames.at(x);
        cout << endl;
        cout << "Enter " << userNames.at(x) << "'s current score: ";
        cin >> currPoints[x][z];
        cout << endl;
        cout << "List " << userNames.at(x) << "'s remaining teams as I ask for them." << endl;
        for (y = 0; y < (numTeams / 2); ++y){
            if (y == 0) {
                cout << "National Champion: ";
                getline(cin, tempString);
            }
            else if (y == 1) {
                cout << "Runner-up: ";
            }
            else if ((y >= 2) && (y < 4)) {
                cout << "Final Four loser (" << y - 1 << "): ";
            }
            else {
                cout << "Elite Eight loser (" << y - 3 << "): ";
            }

            getline(cin, chosenTeam);
            cout << endl;

            remainingTeams.at(x).append(chosenTeam);
            if (y != 7) {
                remainingTeams.at(x).append(", ");
            }
        }

        newPoints.at(x) = currPoints[x][z];
        numWins.at(x) = 0.0;
    }

    cout << endl;

    vector<string> sweetSixteen(numTeams);
    vector<int> sweetSixteenSeeds(numTeams);
    vector<string> eliteEight(sweetSixteen.size() / 2);
    vector<int> eliteEightSeeds(sweetSixteenSeeds.size() / 2);
    vector<string> finalFour(eliteEight.size() / 2);
    vector<int> finalFourSeeds(eliteEightSeeds.size() / 2);
    vector<string> nationalChampionship(finalFour.size() / 2);
    vector<int> nationalChampionshipSeeds(finalFourSeeds.size() / 2);
    string nationalChampion;
    vector<string> eliteEightLosers(eliteEight.size() / 2);
    vector<string> finalFourLosers(finalFour.size() / 2);
    string runnerUp;
    vector<int> gameOrder(sweetSixteen.size() / 2);

    cout << "Enter the Sweet Sixteen teams and their seeds in order."
         << "(If not playing with upset bonus, enter 0 for seeds)" << endl;
    for (x = 0; x < numTeams; ++x) {
        cout << x + 1 << ". Team: ";
        getline(cin, sweetSixteen.at(x));
        cout << endl;
        cout << "Seed: ";
        cin >> sweetSixteenSeeds.at(x);
        cout << endl;
        getline (cin, tempString);
    }
    cout << endl;

    cout << "Enter the order in which the games will be played:" << endl;
    for(y = 0; y < gameOrder.size(); ++y) {
        cout << (y + 1) << ". ";
        cin >> gameOrder.at(y);
        cout << endl;
    }

    unsigned int a;
    unsigned int b;
    unsigned int c;
    unsigned int d;
    unsigned int e;
    unsigned int f;
    unsigned int g;
    unsigned int h;
    unsigned int i;
    unsigned int j;
    unsigned int k;
    unsigned int l;
    unsigned int m;
    unsigned int n;
    unsigned int o;

    for (x = 0; x < numBrackets; ++x) {
        for (a = 0; a < 2; ++a) {
            for (b = 0; b < 2; ++b) {
                for (c = 0; c < 2; ++c) {
                    for (d = 0; d < 2; ++d) {
                        for (e = 0; e < 2; ++e) {
                            for (f = 0; f < 2; ++f) {
                                for (g = 0; g < 2; ++g) {
                                    for (h = 0; h < 2; ++h) {
                                        numWinsPerScenario[x][a][b][c][d][e][f][g][h] = 0.0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    for (x = 0; x < numBrackets; ++x) {
        numWins.at(x) = 0.0;
    }

    for (a = (gameOrder.at(0) - 1) * 2; a < gameOrder.at(0) * 2; ++a) {
        CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight, eliteEightSeeds, a, upsetAlert);
        AwardPoints(newPoints, remainingTeams, eliteEight.at(a / 2), upsetAlert,
                    (64 / sweetSixteen.size()));

        z += 1;
        for (x = 0; x < numBrackets; ++x) {
            currPoints[x][z] = newPoints.at(x);
        }

        for (b = (gameOrder.at(1) - 1) * 2; b < gameOrder.at(1) * 2; ++b) {
            CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight, eliteEightSeeds, b,
                                     upsetAlert);
            AwardPoints(newPoints, remainingTeams, eliteEight.at(b / 2), upsetAlert,
                        (64 / sweetSixteen.size()));

            z += 1;
            for (x = 0; x < numBrackets; ++x) {
                currPoints[x][z] = newPoints.at(x);
            }

            for (c = (gameOrder.at(2) - 1) * 2; c < gameOrder.at(2) * 2; ++c) {
                CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight, eliteEightSeeds, c,
                                         upsetAlert);
                AwardPoints(newPoints, remainingTeams, eliteEight.at(c / 2), upsetAlert,
                            (64 / sweetSixteen.size()));

                z += 1;
                for (x = 0; x < numBrackets; ++x) {
                    currPoints[x][z] = newPoints.at(x);
                }

                for (d = (gameOrder.at(3) - 1) * 2; d < gameOrder.at(3) * 2; ++d) {
                    CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight, eliteEightSeeds, d,
                                             upsetAlert);
                    AwardPoints(newPoints, remainingTeams, eliteEight.at(d / 2), upsetAlert,
                                (64 / sweetSixteen.size()));

                    z += 1;
                    for (x = 0; x < numBrackets; ++x) {
                        currPoints[x][z] = newPoints.at(x);
                    }

                    for (e = (gameOrder.at(4) - 1) * 2; e < gameOrder.at(4) * 2; ++e) {
                        CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight, eliteEightSeeds, e,
                                                 upsetAlert);
                        AwardPoints(newPoints, remainingTeams, eliteEight.at(e / 2), upsetAlert,
                                    (64 / sweetSixteen.size()));

                        z += 1;
                        for (x = 0; x < numBrackets; ++x) {
                            currPoints[x][z] = newPoints.at(x);
                        }

                        for (f = (gameOrder.at(5) - 1) * 2; f < gameOrder.at(5) * 2; ++f) {
                            CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight,
                                                     eliteEightSeeds, f, upsetAlert);
                            AwardPoints(newPoints, remainingTeams, eliteEight.at(f / 2), upsetAlert,
                                        (64 / sweetSixteen.size()));

                            z += 1;
                            for (x = 0; x < numBrackets; ++x) {
                                currPoints[x][z] = newPoints.at(x);
                            }

                            for (g = (gameOrder.at(6) - 1) * 2; g < gameOrder.at(6) * 2; ++g) {
                                CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight,
                                                         eliteEightSeeds, g, upsetAlert);
                                AwardPoints(newPoints, remainingTeams, eliteEight.at(g / 2),
                                            upsetAlert, (64 / sweetSixteen.size()));

                                z += 1;
                                for (x = 0; x < numBrackets; ++x) {
                                    currPoints[x][z] = newPoints.at(x);
                                }

                                for (h = (gameOrder.at(7) - 1) * 2; h < gameOrder.at(7) * 2; ++h) {
                                    CompareTeamsSweetSixteen(sweetSixteen, sweetSixteenSeeds, eliteEight,
                                                             eliteEightSeeds, h, upsetAlert);
                                    AwardPoints(newPoints, remainingTeams, eliteEight.at(h / 2),
                                                upsetAlert, (64 / sweetSixteen.size()));

                                    z += 1;
                                    for (x = 0; x < numBrackets; ++x) {
                                        currPoints[x][z] = newPoints.at(x);
                                    }

                                    for (i = 0; i < 2; ++i) {
                                        CompareTeams(eliteEight, eliteEightSeeds, finalFour, finalFourSeeds,
                                                     eliteEightLosers, i, upsetAlert);
                                        AwardPoints(newPoints, remainingTeams, finalFour.at(i / 2),
                                                    upsetAlert, (64 / eliteEight.size()));

                                        z += 1;
                                        for (x = 0; x < numBrackets; ++x) {
                                            currPoints[x][z] = newPoints.at(x);
                                        }

                                        for (j = 2; j < 4; ++j) {
                                            CompareTeams(eliteEight, eliteEightSeeds, finalFour, finalFourSeeds,
                                                         eliteEightLosers, j, upsetAlert);
                                            AwardPoints(newPoints, remainingTeams,
                                                        finalFour.at(j / 2), upsetAlert,
                                                        (64 / eliteEight.size()));

                                            z += 1;
                                            for (x = 0; x < numBrackets; ++x) {
                                                currPoints[x][z] = newPoints.at(x);
                                            }

                                            for (k = 4; k < 6; ++k) {
                                                CompareTeams(eliteEight, eliteEightSeeds, finalFour,
                                                             finalFourSeeds, eliteEightLosers, k, upsetAlert);
                                                AwardPoints(newPoints, remainingTeams,
                                                            finalFour.at(k / 2), upsetAlert,
                                                            (64 / eliteEight.size()));

                                                z += 1;
                                                for (x = 0; x < numBrackets; ++x) {
                                                    currPoints[x][z] = newPoints.at(x);
                                                }

                                                for (l = 6; l < 8; ++l) {
                                                    CompareTeams(eliteEight, eliteEightSeeds, finalFour,
                                                                 finalFourSeeds, eliteEightLosers, l,
                                                                 upsetAlert);
                                                    AwardPoints(newPoints, remainingTeams,
                                                                finalFour.at(l / 2), upsetAlert,
                                                                (64 / eliteEight.size()));

                                                    z += 1;
                                                    for (x = 0; x < numBrackets; ++x) {
                                                        currPoints[x][z] = newPoints.at(x);
                                                    }

                                                    for (m = 0; m < 2; ++m) {
                                                        CompareTeams(finalFour, finalFourSeeds, nationalChampionship,
                                                                     nationalChampionshipSeeds, finalFourLosers,
                                                                     m, upsetAlert);
                                                        AwardPoints(newPoints, remainingTeams,
                                                                    nationalChampionship.at(m / 2),
                                                                    upsetAlert, (64 / finalFour.size()));

                                                        z += 1;
                                                        for (x = 0; x < numBrackets; ++x) {
                                                            currPoints[x][z] = newPoints.at(x);
                                                        }

                                                        for (n = 2; n < 4; ++n) {
                                                            CompareTeams(finalFour, finalFourSeeds,
                                                                         nationalChampionship,
                                                                         nationalChampionshipSeeds,
                                                                         finalFourLosers, n, upsetAlert);
                                                            AwardPoints(newPoints, remainingTeams,
                                                                        nationalChampionship.at(n / 2),
                                                                        upsetAlert,
                                                                        (64 / finalFour.size()));

                                                            z += 1;
                                                            for (x = 0; x < numBrackets; ++x) {
                                                                currPoints[x][z] = newPoints.at(x);
                                                            }

                                                            for (o = 0; o < 2; ++o) {
                                                                CompareTeamsChampionship(nationalChampionship,
                                                                                         nationalChampionshipSeeds,
                                                                                         nationalChampion,
                                                                                         runnerUp, o,
                                                                                         upsetAlert);
                                                                AwardPoints(newPoints, remainingTeams,
                                                                            nationalChampion, upsetAlert,
                                                                            (64 / nationalChampionship.size()));

                                                                z += 1;
                                                                for (x = 0; x < numBrackets; ++x) {
                                                                    currPoints[x][z] = newPoints.at(x);
                                                                }
                                                                for (y = 0; y < (numTeams / 2); ++y){
                                                                    if (y == 0) {
                                                                        out << left << nationalChampion;
                                                                    }
                                                                    else if (y == 1) {
                                                                        out << runnerUp;
                                                                    }
                                                                    else if ((y >= 2) && (y < 4)) {
                                                                        out << finalFourLosers.at(y - 2);
                                                                    }
                                                                    else {
                                                                        out << eliteEightLosers.at(y - 4);
                                                                    }
                                                                    if (y != 7) {
                                                                        out <<", ";
                                                                    }
                                                                }
                                                                out << " ";

                                                                highScore = currPoints[0][z];
                                                                for (x = 0; x < numBrackets; ++x) {
                                                                    if (currPoints[x][z] > highScore) {
                                                                        highScore = currPoints[x][z];
                                                                    }
                                                                }
                                                                for (x = 0; x < numBrackets; ++x) {
                                                                    if (currPoints[x][z] == highScore) {
                                                                        numWinners += 1;
                                                                        if (numWinners > 1) {
                                                                            out << "/";
                                                                        }
                                                                        out << right << userNames.at(x);
                                                                    }
                                                                }
                                                                out << endl;
                                                                for (x = 0; x < numBrackets; ++x) {
                                                                    if (currPoints[x][z] == highScore) {
                                                                        numWinsPerScenario[x][a % 2][b % 2][c % 2]
                                                                        [d % 2][e % 2][f % 2][g % 2][h % 2] +=
                                                                                (1.0 / numWinners);
                                                                    }
                                                                }

                                                                numWinners = 0.0;
                                                                z -= 1;
                                                                for (x = 0; x < numBrackets; ++x) {
                                                                    newPoints.at(x) = currPoints[x][z];
                                                                }
                                                            }

                                                            z -= 1;
                                                            for (x = 0; x < numBrackets; ++x) {
                                                                newPoints.at(x) = currPoints[x][z];
                                                            }
                                                        }

                                                        z -= 1;
                                                        for (x = 0; x < numBrackets; ++x) {
                                                            newPoints.at(x) = currPoints[x][z];
                                                        }
                                                    }

                                                    z -= 1;
                                                    for (x = 0; x < numBrackets; ++x) {
                                                        newPoints.at(x) = currPoints[x][z];
                                                    }
                                                }

                                                z -= 1;
                                                for (x = 0; x < numBrackets; ++x) {
                                                    newPoints.at(x) = currPoints[x][z];
                                                }
                                            }

                                            z -= 1;
                                            for (x = 0; x < numBrackets; ++x) {
                                                newPoints.at(x) = currPoints[x][z];
                                            }
                                        }

                                        z -= 1;
                                        for (x = 0; x < numBrackets; ++x) {
                                            newPoints.at(x) = currPoints[x][z];
                                        }
                                    }

                                    out << "*" << endl;
                                    for (x = 0; x < numBrackets; ++x) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                                [g % 2][h % 2];
                                        out << userNames.at(x) << ": " << fixed << setprecision(2) << numWins.at(x)
                                        << endl;
                                        numWins.at(x) = 0;
                                    }
                                    out << endl;

                                    z -= 1;
                                    for (x = 0; x < numBrackets; ++x) {
                                        newPoints.at(x) = currPoints[x][z];
                                    }
                                }

                                out << "**" << endl;
                                for (x = 0; x < numBrackets; ++x) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                    out << userNames.at(x) << ": " << numWins.at(x) << endl;
                                    numWins.at(x) = 0;
                                }
                                out << endl;

                                z -= 1;
                                for (x = 0; x < numBrackets; ++x) {
                                    newPoints.at(x) = currPoints[x][z];
                                }
                            }

                            out << "***" << endl;
                            for (x = 0; x < numBrackets; ++x) {
                                for (g = (gameOrder.at(6) - 1) * 2; g < (gameOrder.at(6) * 2); ++g) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                }
                                out << userNames.at(x) << ": " << numWins.at(x) << endl;
                                numWins.at(x) = 0;
                            }
                            out << endl;

                            z -= 1;
                            for (x = 0; x < numBrackets; ++x) {
                                newPoints.at(x) = currPoints[x][z];
                            }
                        }

                        out << "****" << endl;
                        for (x = 0; x < numBrackets; ++x) {
                            for (f = (gameOrder.at(5) - 1) * 2; f < (gameOrder.at(5) * 2); ++f) {
                                for (g = (gameOrder.at(6) - 1) * 2; g < (gameOrder.at(6) * 2); ++g) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                }
                            }
                            out << userNames.at(x) << ": " << numWins.at(x) << endl;
                            numWins.at(x) = 0;
                        }
                        out << endl;

                        z -= 1;
                        for (x = 0; x < numBrackets; ++x) {
                            newPoints.at(x) = currPoints[x][z];
                        }
                    }

                    out << "*****" << endl;
                    for (x = 0; x < numBrackets; ++x) {
                        for (e = (gameOrder.at(4) - 1) * 2; e < (gameOrder.at(4) * 2); ++e) {
                            for (f = (gameOrder.at(5) - 1) * 2; f < (gameOrder.at(5) * 2); ++f) {
                                for (g = (gameOrder.at(6) - 1) * 2; g < (gameOrder.at(6) * 2); ++g) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                }
                            }
                        }
                        out << userNames.at(x) << ": " << numWins.at(x) << endl;
                        numWins.at(x) = 0;
                    }
                    out << endl;

                    z -= 1;
                    for (x = 0; x < numBrackets; ++x) {
                        newPoints.at(x) = currPoints[x][z];
                    }
                }

                out << "******" << endl;
                for (x = 0; x < numBrackets; ++x) {
                    for (d = (gameOrder.at(3) - 1) * 2; d < (gameOrder.at(3) * 2); ++d) {
                        for (e = (gameOrder.at(4) - 1) * 2; e < (gameOrder.at(4) * 2); ++e) {
                            for (f = (gameOrder.at(5) - 1) * 2; f < (gameOrder.at(5) * 2); ++f) {
                                for (g = (gameOrder.at(6) - 1) * 2; g < (gameOrder.at(6) * 2); ++g) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                }
                            }
                        }
                    }
                    out << userNames.at(x) << ": " << numWins.at(x) << endl;
                    numWins.at(x) = 0;
                }
                out << endl;

                z -= 1;
                for (x = 0; x < numBrackets; ++x) {
                    newPoints.at(x) = currPoints[x][z];
                }
            }

            out << "*******" << endl;
            for (x = 0; x < numBrackets; ++x) {
                for (c = (gameOrder.at(2) - 1) * 2; c < (gameOrder.at(2) * 2); ++c) {
                    for (d = (gameOrder.at(3) - 1) * 2; d < (gameOrder.at(3) * 2); ++d) {
                        for (e = (gameOrder.at(4) - 1) * 2; e < (gameOrder.at(4) * 2); ++e) {
                            for (f = (gameOrder.at(5) - 1) * 2; f < (gameOrder.at(5) * 2); ++f) {
                                for (g = (gameOrder.at(6) - 1) * 2; g < (gameOrder.at(6) * 2); ++g) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                }
                            }
                        }
                    }
                }
                out << userNames.at(x) << ": " << numWins.at(x) << endl;
                numWins.at(x) = 0;
            }
            out << endl;

            z -= 1;
            for (x = 0; x < numBrackets; ++x) {
                newPoints.at(x) = currPoints[x][z];
            }
        }

        out << "********" << endl;
        for (x = 0; x < numBrackets; ++x) {
            for (b = (gameOrder.at(1) - 1) * 2; b < (gameOrder.at(1) * 2); ++b) {
                for (c = (gameOrder.at(2) - 1) * 2; c < (gameOrder.at(2) * 2); ++c) {
                    for (d = (gameOrder.at(3) - 1) * 2; d < (gameOrder.at(3) * 2); ++d) {
                        for (e = (gameOrder.at(4) - 1) * 2; e < (gameOrder.at(4) * 2); ++e) {
                            for (f = (gameOrder.at(5) - 1) * 2; f < (gameOrder.at(5) * 2); ++f) {
                                for (g = (gameOrder.at(6) - 1) * 2; g < (gameOrder.at(6) * 2); ++g) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                }
                            }
                        }
                    }
                }
            }
            out << userNames.at(x) << ": " << numWins.at(x) << endl;
            numWins.at(x) = 0;
        }
        out << endl;

        z -= 1;
        for (x = 0; x < numBrackets; ++x) {
            newPoints.at(x) = currPoints[x][z];
        }
    }

    out << "*********" << endl;
    for (x = 0; x < numBrackets; ++x) {
        for (a = (gameOrder.at(0) - 1) * 2; a < (gameOrder.at(0) * 2); ++a) {
            for (b = (gameOrder.at(1) - 1) * 2; b < (gameOrder.at(1) * 2); ++b) {
                for (c = (gameOrder.at(2) - 1) * 2; c < (gameOrder.at(2) * 2); ++c) {
                    for (d = (gameOrder.at(3) - 1) * 2; d < (gameOrder.at(3) * 2); ++d) {
                        for (e = (gameOrder.at(4) - 1) * 2; e < (gameOrder.at(4) * 2); ++e) {
                            for (f = (gameOrder.at(5) - 1) * 2; f < (gameOrder.at(5) * 2); ++f) {
                                for (g = (gameOrder.at(6) - 1) * 2; g < (gameOrder.at(6) * 2); ++g) {
                                    for (h = (gameOrder.at(7) - 1) * 2; h < (gameOrder.at(7) * 2); ++h) {
                                        numWins.at(x) += numWinsPerScenario[x][a % 2][b % 2][c % 2][d % 2][e % 2][f % 2]
                                        [g % 2][h % 2];
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        out << userNames.at(x) << ": " << numWins.at(x) << endl;
        numWins.at(x) = 0;
    }
    out << endl;

    out.close();

    return 0;
}
