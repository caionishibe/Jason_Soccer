package br.ufrgs.f180.api.model;

import java.util.List;

public class GameInformation {

	private List<RobotInformation> teamA;
	private List<RobotInformation> teamB;
	private BallInformation ball;

	public List<RobotInformation> getTeamA() {
		return teamA;
	}

	public void setTeamA(List<RobotInformation> teamA) {
		this.teamA = teamA;
	}

	public List<RobotInformation> getTeamB() {
		return teamB;
	}

	public void setTeamB(List<RobotInformation> teamB) {
		this.teamB = teamB;
	}

	public BallInformation getBall() {
		return ball;
	}

	public void setBall(BallInformation ball) {
		this.ball = ball;
	}

}
