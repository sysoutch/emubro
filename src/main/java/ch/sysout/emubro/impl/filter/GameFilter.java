package ch.sysout.emubro.impl.filter;

import ch.sysout.emubro.api.filter.Criteria;
import ch.sysout.emubro.api.filter.Filter;
import ch.sysout.emubro.api.model.Game;

public class GameFilter implements Filter<Game> {

	// Filter<Game> filter = e.getSource();
	// List<Game> games = e.getObjects();
	// Criteria criteria = e.getCriteria();
	//
	// for (Game game : games) {
	// if (filter.match(criteria, game)) {
	// // ((GameTableModel) mdlTblGames).addGame(game);
	// }
	// }
	//
	@Override
	public boolean match(Criteria criteria, Game game) {
		String gameTitle = game.getName().toLowerCase();
		String criteriaText = criteria.getText().toLowerCase();
		if (gameTitle.contains(criteriaText)) {
			return true;
		}
		return false;
	}
}