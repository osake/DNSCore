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
package de.uzk.hki.da.cb;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.NotImplementedException;

import de.uzk.hki.da.core.IngestGate;
import de.uzk.hki.da.grid.DistributedConversionAdapter;
import de.uzk.hki.da.grid.GridFacade;
import de.uzk.hki.da.service.RetrievePackagesHelper;

/**
 * @author Daniel M. de Oliveira
 */
public class ObjectToWorkAreaAction extends AbstractAction {

	private IngestGate ingestGate;
	private GridFacade gridFacade;
	private DistributedConversionAdapter distributedConversionAdapter;
	
	
	@Override
	boolean implementation() {
		
		new File(object.getDataPath()).mkdirs();
		
		RetrievePackagesHelper retrievePackagesHelper = new RetrievePackagesHelper(getGridFacade());
		
		try {
			if (!ingestGate.canHandle(retrievePackagesHelper.getObjectSize(object, job))) {
				logger.info("no disk space available at working resource. will not fetch new data.");
				return false;
			}
		} catch (IOException e) {
			throw new RuntimeException("Failed to determine object size for object " + object.getIdentifier(), e);
		}
		
		try {
			retrievePackagesHelper.loadPackages(object, true);
		} catch (IOException e) {
			throw new RuntimeException("error while trying to get existing packages from lza area",e);
		}
		
		distributedConversionAdapter.register("fork/"+object.getContractor().getShort_name()+"/"+object.getIdentifier(),
				object.getPath());
		return true;
	}

	
	
	
	
	@Override
	void rollback() throws Exception {
		throw new NotImplementedException("No rollback implemented for this action");
	}





	public IngestGate getIngestGate() {
		return ingestGate;
	}





	public void setIngestGate(IngestGate ingestGate) {
		this.ingestGate = ingestGate;
	}





	public DistributedConversionAdapter getDistributedConversionAdapter() {
		return distributedConversionAdapter;
	}





	public void setDistributedConversionAdapter(
			DistributedConversionAdapter distributedConversionAdapter) {
		this.distributedConversionAdapter = distributedConversionAdapter;
	}





	public GridFacade getGridFacade() {
		return gridFacade;
	}





	public void setGridFacade(GridFacade gridFacade) {
		this.gridFacade = gridFacade;
	}

}
