package com.dalonedrow.rpg.base.flyweights;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RPGExceptionTest {
	@Test
	public void canCreate() {
		RPGException ex =
				new RPGException(ErrorMessage.BAD_PARAMETERS,
						new Exception("msg"));
		assertEquals(ex.getDeveloperMessage(), "msg");
		assertEquals(ex.getErrorMessage(), ErrorMessage.BAD_PARAMETERS);
		
		ex = new RPGException(ErrorMessage.ILLEGAL_ACCESS, "msg");
		assertEquals(ex.getDeveloperMessage(), "msg");
		assertEquals(ex.getErrorMessage(), ErrorMessage.ILLEGAL_ACCESS);
		
		ex = new RPGException(ErrorMessage.INTERNAL_ERROR, "dev", new Exception("msg"));
		assertEquals("msg", ex.getDeveloperMessage());
		assertEquals(ex.getErrorMessage(), ErrorMessage.INTERNAL_ERROR);
	}
}
