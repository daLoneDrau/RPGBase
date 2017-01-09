package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class QuestTest {
	Quest main;
	@Before
	public void before() {
		main = new Quest("main", "Main Quest");
		QuestBranch qb = new QuestBranch(
				"root", "Recover the sword of Fargaol", 0);
		main.setRoot(qb);
		qb = new QuestBranch(
				"goodPath1", "Rescue Princess", 50);
		main.getBranch("root").addBranch(qb);
		qb = new QuestBranch(
				"goodPath2", "Clear Spring", 50);
		main.getBranch("root").addBranch(qb);
		qb = new QuestBranch(
				"badPath", "Slaughter villagers", 50);
		main.getBranch("root").addBranch(qb);
		qb = new QuestBranch(
				"goodPath1a", "Clear Spring", 50);
		main.getBranch("goodPath1").addBranch(qb);
		qb = new QuestBranch(
				"goodPath2a", "Rescue Princess", 50);
		main.getBranch("goodPath2").addBranch(qb);
		qb = new QuestBranch(
				"badPathEnd", "Sword recovered", 100);
		main.getBranch("badPath").addBranch(qb);
		qb = new QuestBranch(
				"goodPath1End", "Sword recovered", 100);
		main.getBranch("goodPath1a").addBranch(qb);
		qb = new QuestBranch(
				"goodPath2End", "Sword recovered", 100);
		main.getBranch("goodPath2a").addBranch(qb);
	}
	@Test
	public void canCompleteQuest() throws RPGException {
		main.getBranch("goodPath1").setTaken(true);
		main.getBranch("goodPath1a").setTaken(true);
		main.getBranch("goodPath1End").setTaken(true);
		assertTrue("Quest is complete", main.isComplete());
	}
	@Test
	public void canConstruct() {
		assertEquals("Ident is main", main.getIdent(), "main");
		assertEquals("Title is Main Quest", main.getTitle(), "Main Quest");
	}
	@Test
	public void canFindRootFromBranch() {
		QuestBranch qb = main.getBranch("badPathEnd").getRoot();
		assertEquals("badPathEnd root is badPath", qb.getRefId(), "badPath");
		assertEquals("badPath is worth 100xp", qb.getXpReward(), 50);
	}
	@Test
	public void canGetBranch() {
		QuestBranch qb = main.getBranch("badPathEnd");
		assertEquals("Got bad path end", qb.getLocalized(), "Sword recovered");
	}
	@Test
	public void canGetString() throws RPGException {
		assertEquals("Main string", main.toString(),
				"Recover the sword of Fargaol\n");
		main.getBranch("goodPath1").setTaken(true);
		main.getBranch("goodPath1a").setTaken(true);
		main.getBranch("goodPath1End").setTaken(true);
		assertEquals("Main string", main.toString(),
				"Recover the sword of Fargaol\n\nRescue Princess"
						+ "\n\nClear Spring\n\nSword recovered\n");
	}
	@Test
	public void canTakeBranch() throws RPGException {
		QuestBranch qb = main.getBranch("goodPath1");
		qb.setTaken(true);
		assertTrue("Took good path 1", main.getBranch("goodPath1").wasTaken());
	}
	@Test
	public void hasRoot() {
		assertEquals("Root id is root", main.getRoot().getRefId(), "root");
	}
	@Test
	public void willNotCompleteQuestEarly() throws RPGException {
		assertFalse("Quest is not complete", main.isComplete());
		main.getBranch("goodPath1").setTaken(true);
		assertFalse("Quest is not complete", main.isComplete());
	}
	@Test(expected = RPGException.class)
	public void willNotTakeBranchAfterSibling() throws RPGException {
		main.getBranch("goodPath1").setTaken(true);
		main.getBranch("goodPath2").setTaken(true);
	}
	@Test(expected = RPGException.class)
	public void willNotTakeBranchBeforeParent() throws RPGException {
		main.getBranch("badPathEnd").setTaken(true);
	}
	@Test(expected = NullPointerException.class)
	public void willNotTakeMissingBranch() throws RPGException {
		main.getBranch("missing").setTaken(true);
	}
}
