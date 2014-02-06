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

package de.uzk.hki.da.convert;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import de.uzk.hki.da.model.ConversionInstruction;
import de.uzk.hki.da.model.DAFile;
import de.uzk.hki.da.model.Event;
import de.uzk.hki.da.model.Object;
import de.uzk.hki.da.model.Package;
import de.uzk.hki.da.model.VideoRestriction;
import de.uzk.hki.da.utils.SimplifiedCommandLineConnector;
import de.uzk.hki.da.utils.Utilities;


/**
 * The Class PublishVideoConversionStrategy
 * @author Thomas Kleinke
 * @author Christian Weitz
 * @author Daniel M. de Oliveira
 */
public class PublishVideoConversionStrategy extends PublishConversionStrategyBase {

	/** The logger. */
	private static Logger logger = 
			LoggerFactory.getLogger(PublishVideoConversionStrategy.class);
	
	/** The cli connector. */
	private SimplifiedCommandLineConnector cliConnector;
	
	/** The pkg. */
	private Package pkg;

	/* (non-Javadoc)
	 * @see de.uzk.hki.da.convert.ConversionStrategy#convertFile(de.uzk.hki.da.model.ConversionInstruction)
	 */
	@Override
	public List<Event> convertFile(ConversionInstruction ci)
			throws FileNotFoundException {
		if (pkg==null) throw new IllegalStateException("pkg not set");
		if (cliConnector==null) throw new IllegalStateException("cliConnector not set");
		
		List<Event> results = new ArrayList<Event>();
		
		new File(object.getDataPath()+"dip/public/"+ci.getTarget_folder()).mkdirs();
		new File(object.getDataPath()+"dip/institution/"+ci.getTarget_folder()).mkdirs();
		
		// convert public datastreams
		String cmdPUBLIC[] = new String[]{
				"HandBrakeCLI",
				"-i",
				"\"" + ci.getSource_file().toRegularFile().getAbsolutePath() + "\"",
				"-o",
				"\"" + object.getDataPath()+"dip/public/"+ci.getTarget_folder()+FilenameUtils.getBaseName(ci.getSource_file().toRegularFile().getAbsolutePath())+".mp4\"",
				"-e","x264","-f","mp4","-E","faac"
		};
		
		if (!cliConnector.execute((String[]) ArrayUtils.addAll(cmdPUBLIC,getRestrictionParametersForAudience("PUBLIC")))){
			throw new RuntimeException("command not succeeded");
		}
		DAFile pubFile = new DAFile(pkg, "dip/public", Utilities.slashize(ci.getTarget_folder()) + 
				FilenameUtils.getBaseName(ci.getSource_file().toRegularFile().getAbsolutePath()) + ".mp4");
		
		Event e = new Event();
		e.setType("CONVERT");
		e.setDetail(Utilities.createString(cmdPUBLIC));
		e.setSource_file(ci.getSource_file());
		e.setTarget_file(pubFile);
		e.setDate(new Date());
		
		String cmdINSTITUTION[] = new String[]{
				"HandBrakeCLI",
				"-i",
				"\"" + ci.getSource_file().toRegularFile().getAbsolutePath() + "\"",
				"-o",
				"\"" + object.getDataPath()+"dip/institution/"+ci.getTarget_folder()+FilenameUtils.getBaseName(ci.getSource_file().toRegularFile().getAbsolutePath())+".mp4\"",
				"-e","x264","-f","mp4","-E","faac"
		};

		String[] cmd = (String[]) ArrayUtils.addAll(cmdINSTITUTION,getRestrictionParametersForAudience("INSTITUTION"));
		if (!cliConnector.execute(cmd))
			throw new RuntimeException("command not succeeded:" + Arrays.toString(cmd));
		
		DAFile instFile = new DAFile(pkg, "dip/institution", Utilities.slashize(ci.getTarget_folder()) + 
				FilenameUtils.getBaseName(ci.getSource_file().toRegularFile().getAbsolutePath()) + ".mp4");
		Event e2 = new Event();
		e2.setType("CONVERT");
		e2.setDetail(Utilities.createString(cmdINSTITUTION));
		e2.setSource_file(ci.getSource_file());
		e2.setTarget_file(instFile);
		
		results.add(e);
		results.add(e2);
		
		return results;
	}
	
	/**
	 * Gets the restriction parameters for audience.
	 *
	 * @param audience the audience
	 * @return the restriction parameters for audience
	 */
	private String[] getRestrictionParametersForAudience(String audience) {
		
		if (getPublicationRightForAudience(audience) == null)
			return new String[]{};
		
		VideoRestriction videoRestriction = getPublicationRightForAudience(audience).getVideoRestriction();
		if (videoRestriction == null)
			return new String[]{};
		
		String height = videoRestriction.getHeight();
		logger.debug("height restriction for audience " + audience + ": " + height);
				
		String duration = "";
		if (videoRestriction.getDuration() != null)
			duration = videoRestriction.getDuration().toString();
		logger.debug("duration restriction for audience " + audience + ": " + duration);		
		
		if (height != null && !height.equals("") &&
			duration != null && !duration.equals(""))
			return new String[]{"-l", height, "--stop-at", "duration:" + duration};
		
		if (duration != null && !duration.equals(""))
			return new String[]{"--stop-at", "duration:" + duration};
		
		if (height != null && !height.equals(""))
			return new String[]{"-l", height};

		return new String[]{};
	}
	
	/* (non-Javadoc)
	 * @see de.uzk.hki.da.convert.ConversionStrategy#setDom(org.w3c.dom.Document)
	 */
	public void setDom(Document dom){}
	

	/* (non-Javadoc)
	 * @see de.uzk.hki.da.convert.ConversionStrategy#setCLIConnector(de.uzk.hki.da.convert.CLIConnector)
	 */
	public void setCLIConnector(SimplifiedCommandLineConnector cliConnector){
		this.cliConnector = cliConnector;
	}
	
	/* (non-Javadoc)
	 * @see de.uzk.hki.da.convert.ConversionStrategy#setParam(java.lang.String)
	 */
	@Override
	public void setParam(String param) {}


	/* (non-Javadoc)
	 * @see de.uzk.hki.da.convert.ConversionStrategy#setObject(de.uzk.hki.da.model.Object)
	 */
	@Override
	public void setObject(Object obj) {
		this.object = obj;
		this.pkg = obj.getLatestPackage();
	}

}
