/*
  DA-NRW Software Suite | ContentBroker
  Copyright (C) 2013 Historisch-Kulturwissenschaftliche Informationsverarbeitung
  Universität zu Köln

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.uzk.hki.da.at;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uzk.hki.da.model.Object;

/**
 * @author Daniel M. de Oliveira
 */
public class ATPIPGen extends Base{

	@Before
	public void setUp() throws IOException{
		setUpBase();
	}
	
	@After
	public void tearDown() throws IOException{
		clearDB();
		cleanStorage();
	}

	/**
	 * @author ???
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws JDOMException 
	 */
	@Test
	public void testUpdateUrls() throws InterruptedException, IOException, JDOMException{
		String name = "UpdateUrls";
		createObjectAndJob("ATPIPGen"+name,"700");
		waitForJobsToFinish("ATPIPGen"+name, 500);
		Object object = fetchObjectFromDB("ATPIPGen"+name);
		String dipPath = object.getIdentifier()+"/"; 
		
		
		
		assertTrue(new File(dipAreaRootPath+"_data/danrw/"+dipPath+"_0c32b463b540e3fee433961ba5c491d6.jpg").exists());
		assertTrue(new File(dipAreaRootPath+"_data/danrw-closed/"+dipPath+"_0c32b463b540e3fee433961ba5c491d6.jpg").exists());
		assertTrue(new File(dipAreaRootPath+"_data/danrw/"+dipPath+"METS").exists());
		assertTrue(new File(dipAreaRootPath+"_data/danrw-closed/"+dipPath+"METS").exists());
		
		Namespace METS_NS = Namespace.getNamespace("http://www.loc.gov/METS/");
		Namespace XLINK_NS = Namespace.getNamespace("http://www.w3.org/1999/xlink");
		
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new FileReader(new File(dipAreaRootPath+"_data/danrw/"+dipPath+"METS")));

		String url = doc.getRootElement()
				.getChild("fileSec", METS_NS)
				.getChild("fileGrp", METS_NS)
				.getChild("file", METS_NS)
				.getChild("FLocat", METS_NS)
				.getAttributeValue("href", XLINK_NS);
		
		assertEquals("_0c32b463b540e3fee433961ba5c491d6.jpg", url);
		
		doc = builder.build(new FileReader(new File(dipAreaRootPath+"_data/danrw-closed/"+dipPath+"METS")));

		url = doc.getRootElement()
				.getChild("fileSec", METS_NS)
				.getChild("fileGrp", METS_NS)
				.getChild("file", METS_NS)
				.getChild("FLocat", METS_NS)
				.getAttributeValue("href", XLINK_NS);
		
		assertEquals("_0c32b463b540e3fee433961ba5c491d6.jpg", url);
	}
	
	@Test
	public void testPublishInstOnly() throws InterruptedException, IOException{
		String name = "InstOnly";
		createObjectAndJob("ATPIPGen"+name,"700");
		waitForJobsToFinish("ATPIPGen"+name, 500);
		Object object = fetchObjectFromDB("ATPIPGen"+name);
		String dipPath = object.getIdentifier()+"/"; 
		
		assertFalse(new File(dipAreaRootPath+"_data/danrw/"+dipPath).exists());
		assertTrue( new File(dipAreaRootPath+"_data/danrw-closed/"+dipPath+"_0c32b463b540e3fee433961ba5c491d6.jpg").exists());
	}
	
	@Test
	public void testNoPubWithLawSet() throws InterruptedException, IOException{
		String name = "NoPubWithLawSet";
		createObjectAndJob("ATPIPGen"+name,"700");
		waitForJobsToFinish("ATPIPGen"+name, 500);
		Object object = fetchObjectFromDB("ATPIPGen"+name);
		String dipPath = object.getIdentifier()+"/"; 
		
		assertFalse(new File(dipAreaRootPath+"_data/danrw/"+dipPath).exists());
	}
	
	@Test
	public void testNoPubWithStartDateSet() throws InterruptedException, IOException{
		String name = "NoPubWithStartDateSet";
		createObjectAndJob("ATPIPGen"+name,"700");
		waitForJobsToFinish("ATPIPGen"+name, 500);
		Object object = fetchObjectFromDB("ATPIPGen"+name);
		String dipPath = object.getIdentifier()+"/"; 
		
		assertFalse(new File(dipAreaRootPath+"_data/danrw/"+dipPath).exists());
	}
	
	
	@Test
	public void testPublishNothing() throws InterruptedException, IOException{
		String name = "PublishNothing";
		createObjectAndJob("ATPIPGen"+name,"700");
		waitForJobsToFinish("ATPIPGen"+name,  500);
		Object object = fetchObjectFromDB("ATPIPGen"+name);
		String dipPath = object.getIdentifier()+"/";
		
		assertFalse(new File(dipAreaRootPath+"_data/danrw/"+dipPath).exists());
		assertFalse(new File(dipAreaRootPath+"_data/danrw-closed/"+dipPath).exists());
	}
	
	@Test
	public void testPublishAll() throws InterruptedException, IOException{
		String name = "AllPublic";
		createObjectAndJob("ATPIPGen"+name,"700");
		waitForJobsToFinish("ATPIPGen"+name,  500);
		Object object = fetchObjectFromDB("ATPIPGen"+name);
		String dipPath = object.getIdentifier()+"/"; 
		
		assertTrue(new File(dipAreaRootPath+"_data/danrw/"+dipPath+"_0c32b463b540e3fee433961ba5c491d6.jpg").exists());
		assertTrue(new File(dipAreaRootPath+"_data/danrw-closed/"+dipPath+"_0c32b463b540e3fee433961ba5c491d6.jpg").exists());
	}
	
}
