public class BowlingGame {

	public int getBowlingScore(String bowlingCode) {
		// Step1:parse the data
		String[] formal_extra = bowlingCode.split("\\|\\|");
		String[] formal = formal_extra[0].split("\\|");// the text of each lattice
		String extra = "";// the text of extra chances
		if (formal_extra.length > 1) {
			extra += formal_extra[1];
		}

		// Step2:initialize each lattice
		String[] labels = new String[formal.length];// the label of each lattice
		int[] scores = new int[formal.length];// the score of each lattice
		initialize(formal, labels, scores);

		// Step3:update each lattice
		update(labels, scores, extra);

		// Step4:compute the sum score
		int sum_score = computeSumScore(scores);
		return sum_score;
	}

	/**
	 * initialize the label and score of each lattice
	 * @param formal the text of each lattice
	 * @param labels the label of each lattice
	 * @param scores the score of each lattice
	 */
	public void initialize(String[] formal, String[] labels, int[] scores) {
		for (int i = 0; i < formal.length; i++) {
			String text = formal[i];
			// the lattice is strike
			if (text.length() == 1) {
				labels[i] = "strike";
				scores[i] = 10;
			}
			// the lattice is spare
			else if (text.contains("/")) {
				String score = "";
				char[] chances = text.toCharArray();
				for (int j = 0; j < chances.length; j++) {
					if (chances[j] != '/') {
						score += chances[j];
					} else {
						score += Integer.toString(10 - (chances[j - 1] - 48));
					}
				}
				labels[i] = "spare";
				scores[i] = Integer.parseInt(score);
			}
			// the sum scores of two chances is less than 10
			else {
				String score = "";
				char[] chances = text.toCharArray();
				for (int j = 0; j < chances.length; j++) {
					if (chances[j] != '-') {
						score += chances[j];
					} else {
						score += "0";
					}
				}
				labels[i] = "miss";
				scores[i] = Integer.parseInt(score);
			}
		}
	}

	/**
	 * update the label and score of each lattice
	 * @param labels the label of each lattice
	 * @param scores the score of each lattice
	 * @param extra the text of extra chances
	 */
	public void update(String[] labels, int[] scores, String extra) {
		// update first eight lattices
		for (int i = 0; i < labels.length - 2; i++) {
			computeAccordingRule1(labels, scores, i);
		}
		// update last two lattices,and if there is no extra chances
		if (extra.length() == 0) {
			computeAccordingRule1(labels, scores, labels.length - 2);
			computeAccordingRule1(labels, scores, labels.length - 1);
		}
		// if there is one extra chance
		else if (extra.length() == 1) {
			computeAccordingRule1(labels, scores, labels.length - 2);
			scores[labels.length - 1] = 10;
			computeAccordingRule2(scores, labels.length - 1, extra);
		}
		// if there is two chances
		else {
			computeAccordingRule3(scores, labels.length - 2, extra);
		}
	}

	/**
	 * compute score of each lattice according the rule1
	 * @param labels the label of each lattice
	 * @param scores the score of each lattice
	 * @param i the index of lattice
	 */
	public void computeAccordingRule1(String[] labels, int[] scores, int i) {
		// the lattice is strike
		if (labels[i].equals("strike")) {
			// the next chance is strike
			if (labels[i + 1].equals("strike")) {
				scores[i] += 10;
				// the next next chance is strike
				if (labels[i + 2].equals("strike")) {
					scores[i] += 10;
				}
				// the next next chance is spare or miss
				else {
					scores[i] += (scores[i + 2] / 10);
				}
			}
			// the next chance is spare or miss
			else {
				scores[i] += (scores[i + 1] / 10) + (scores[i + 1] % 10);
			}
		}
		// the lattice is spare
		else if (labels[i].equals("spare")) {
			// the next chance is strike
			if (labels[i + 1].equals("strike")) {
				scores[i] = 20;
			}
			// the next chance is spare or miss
			else {
				scores[i] = 10 + (scores[i + 1] / 10);
			}
		}
		// the lattice is miss
		else {
			scores[i] = (scores[i] / 10) + (scores[i] % 10);
		}
	}

	/**
	 * compute score of last lattice according the rule2
	 * @param scores the score of each lattice
	 * @param i the index last lattice
	 * @param extra the text of extra chance
	 */
	public void computeAccordingRule2(int[] scores, int i, String extra) {
		// the extra chance is strike
		if (extra.equals("X")) {
			scores[i] += 10;
		}
		// the extra chance is spare
		else if (extra.equals("/")) {
			scores[i] = 20;
		}
		// the extra chance is number
		else {
			scores[i] += Integer.parseInt(extra);
		}
	}

	/**
	 * compute score of last two lattices according the rule3
	 * @param scores the score of each lattice
	 * @param i the index second last lattice
	 * @param extra the text of extra chance
	 */
	public void computeAccordingRule3(int[] scores, int i, String extra) {
		char[] chances = extra.toCharArray();
		String first = "" + chances[0];
		String second = "" + chances[1];
		// update the second last lattice
		scores[i] = 20;
		computeAccordingRule2(scores, i, first);
		// update the last lattice
		scores[i + 1] = 10;
		computeAccordingRule2(scores, i + 1, first);
		computeAccordingRule2(scores, i + 1, second);
	}

	/**
	 * compute sum score
	 * @param scores the score of each lattice
	 */
	public int computeSumScore(int[] scores) {
		int sum_score = 0;
		for (int i = 0; i < scores.length; i++) {
			sum_score += scores[i];
		}
		return sum_score;
	}

}
