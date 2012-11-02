package org.syfsyf.search;

import static org.junit.Assert.*;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

public abstract class BaseTest {

	@Before
	public void setup()
	{
		BasicConfigurator.configure();
	}
	
	

}
