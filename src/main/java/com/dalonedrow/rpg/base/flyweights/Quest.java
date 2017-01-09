/**
 *
 */
package com.dalonedrow.rpg.base.flyweights;

import com.dalonedrow.pooled.PooledException;
import com.dalonedrow.pooled.PooledStringBuilder;
import com.dalonedrow.pooled.StringBuilderPool;
import com.dalonedrow.utils.ArrayUtilities;

/**
 * Need to implement this as a tree structure.
 * @author Donald
 */
public final class Quest {
	/** the quest id. */
	private final String	ident;
	/** the root. */
	private QuestBranch		root;
	/** the quest title. */
	private final String	title;
	/**
	 * Creates a new instance of {@link Quest}.
	 * @param id the quest id
	 * @param t the quest title
	 */
	public Quest(final String id, final String t) {
		ident = id;
		title = t;
	}
	/**
	 * Finds a branch with a specific id.
	 * @param id the branch's id
	 * @return {@link QuestBranch} or {@link null} if none was found
	 */
	public QuestBranch getBranch(final String id) {
		// loop through all branches until id is found.
		QuestBranch found = null;
		if (root.getRefId().equalsIgnoreCase(id)) {
			found = root;
		} else {
			found = root.findChild(id);
		}
		return found;
	}
	/**
	 * Gets the quest id.
	 * @return {@link String}
	 */
	public String getIdent() {
		return ident;
	}
	/**
	 * Gets the value for the root.
	 * @return {@link QuestBranch}
	 */
	public QuestBranch getRoot() {
		return root;
	}
	/**
	 * Gets the quest title.
	 * @return {@link String}
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Determines if a {@link Quest} was completed.
	 * @return <tt>true</tt> if the {@link Quest} was completed; <tt>false</tt>
	 *         otherwise
	 */
	public boolean isComplete() {
		// loop through all branches taken until no more children are left.
		boolean complete = true;
		QuestBranch branch = root;
		while (branch.getBranches().length > 0) {
			int i = branch.getBranches().length - 1;
			for (; i >= 0; i--) {
				// go through all branches
				if (branch.getBranches()[i].wasTaken()) {
					// follow branch that was taken
					branch = branch.getBranches()[i];
					break;
				}
			}
			if (i == -1) {
				// made it through all branches, but none were taken yet
				complete = false;
				break;
			}
		}
		// complete should only be true if last branch taken has no children
		return complete;
	}
	/**
	 * Sets the value of the root.
	 * @param branch the new value to set
	 */
	public void setRoot(final QuestBranch branch) {
		root = branch;
		try {
			root.setTaken(true);
		} catch (RPGException e) {
			// we'll NEEEVER get here
			e.printStackTrace();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		PooledStringBuilder sb =
				StringBuilderPool.getInstance().getStringBuilder();
		try {
			QuestBranch branch = root;
			sb.append(branch.getLocalized());
			sb.append('\n');
			while (branch.getBranches().length > 0) {
				int i = branch.getBranches().length - 1;
				for (; i >= 0; i--) {
					// go through all branches
					if (branch.getBranches()[i].wasTaken()) {
						// follow branch that was taken
						branch = branch.getBranches()[i];
						sb.append('\n');
						sb.append(branch.getLocalized());
						sb.append('\n');
						break;
					}
				}
				if (i == -1) {
					// made it through all branches
					break;
				}
			}
		} catch (PooledException e) {
			e.printStackTrace();
		}
		String s = sb.toString();
		sb.returnToPool();
		return s;
	}
}

/**
 * Node structure for the {@link Quest} tree.
 * @author drau
 */
final class QuestBranch {
	/** the list of branches this branch leads to. */
	private QuestBranch[]	branches;
	/** the localized quest text. */
	private final String	localized;
	/** the branch's reference id. a {@link String. */
	private final String	refId;
	/** the branch's root. */
	private QuestBranch		root;
	/** flag indicating the branch has been taken. */
	private boolean			taken;
	/** the XP reward given when the branch is taken. */
	private final long		xpReward;
	/**
	 * Creates a new instance of {@link QuestBranch}.
	 * @param id the branch's reference id
	 * @param text the localized quest text
	 * @param xp the XP reward given when the branch is taken
	 */
	QuestBranch(final String id, final String text, final long xp) {
		refId = id;
		localized = text;
		xpReward = xp;
		branches = new QuestBranch[0];
	}
	/**
	 * Adds a branch this {@link QuestBranch} leads to.
	 * @param branch the new {@link QuestBranch}
	 */
	public void addBranch(final QuestBranch branch) {
		branches = ArrayUtilities.getInstance().extendArray(branch, branches);
		branch.setRoot(this);
	}
	/**
	 * Finds a child branch with a specific id.
	 * @param id the branch's id
	 * @return {@link QuestBranch} or {@link null} if none was found
	 */
	protected QuestBranch findChild(final String id) {
		QuestBranch child = null;
		for (int i = branches.length - 1; i >= 0; i--) {
			if (branches[i].getRefId().equalsIgnoreCase(id)) {
				child = branches[i];
				break;
			}
			child = branches[i].findChild(id);
			if (child != null) {
				break;
			}
		}
		return child;
	}
	/**
	 * Gets the list of branches this branch leads to.
	 * @return {@link QuestBranch[]}
	 */
	public QuestBranch[] getBranches() {
		return branches;
	}
	/**
	 * Gets the localized quest text.
	 * @return {@link String}
	 */
	public String getLocalized() {
		return localized;
	}
	/**
	 * Gets the branch's reference id.
	 * @return {@link String}
	 */
	public String getRefId() {
		return refId;
	}
	/**
	 * Gets the branch's root.
	 * @return {@link QuestBranch}
	 */
	public QuestBranch getRoot() {
		return root;
	}
	/**
	 * Gets the XP reward given when the branch is taken.
	 * @return {@link long}
	 */
	public long getXpReward() {
		return xpReward;
	}
	/**
	 * Sets the branch's root.
	 * @param branch the new value to set
	 */
	public void setRoot(final QuestBranch branch) {
		root = branch;
	}
	/**
	 * Sets the flag indicating the branch has been taken.
	 * @param flag the new flag to set
	 * @throws RPGException if the parent branch was not taken
	 */
	public void setTaken(final boolean flag) throws RPGException {
		if (root != null) {
			if (!root.wasTaken()) {
				throw new RPGException(ErrorMessage.ILLEGAL_OPERATION,
						"Cannot take a child branch before its parent.");
			}
			for (int i = root.getBranches().length - 1; i >= 0; i--) {
				if (root.getBranches()[i].wasTaken()) {
					throw new RPGException(ErrorMessage.ILLEGAL_OPERATION,
							"A branch on this level was already taken.");
				}
			}
		}
		taken = flag;
	}
	/**
	 * Determines if the branch has been taken.
	 * @return <tt>true</tt> if the branch was taken; <tt>false</tt> otherwise
	 */
	public boolean wasTaken() {
		return taken;
	}
}
