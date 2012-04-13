/*
 ###############################################################################
 #                                                                             #
 #    Copyright (C) 2011-2012 OpenMEAP, Inc.                                   #
 #    Credits to Jonathan Schang & Robert Thacher                              #
 #                                                                             #
 #    Released under the LGPLv3                                                #
 #                                                                             #
 #    OpenMEAP is free software: you can redistribute it and/or modify         #
 #    it under the terms of the GNU Lesser General Public License as published #
 #    by the Free Software Foundation, either version 3 of the License, or     #
 #    (at your option) any later version.                                      #
 #                                                                             #
 #    OpenMEAP is distributed in the hope that it will be useful,              #
 #    but WITHOUT ANY WARRANTY; without even the implied warranty of           #
 #    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the            #
 #    GNU Lesser General Public License for more details.                      #
 #                                                                             #
 #    You should have received a copy of the GNU Lesser General Public License #
 #    along with OpenMEAP.  If not, see <http://www.gnu.org/licenses/>.        #
 #                                                                             #
 ###############################################################################
 */

package com.openmeap.model.dto;

import javax.persistence.*;

import com.openmeap.model.AbstractModelEntity;
import com.openmeap.model.ModelEntity;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity object to represent a SLIC application installation instance.
 * @author schang
 */
@Entity @Table(name="application_installation")
public class ApplicationInstallation extends AbstractModelEntity {
	private String uuid;
	private ApplicationVersion applicationVersion; 
	private Date deploymentDate;
	private Date authDate;
	
	/**
	 * @return A uuid generated by the server on the devices first connection
	 */
	@Id @Column(name="uuid")
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Transient public String getPk() { return getUuid(); }
	public void setPk( Object pkValue ) { setUuid((String)pkValue); }
	
	/**
	 * @return The last version successfully deployed to the device
	 */
	@ManyToOne(fetch=FetchType.EAGER,cascade={}) @JoinColumn(name="version_id")
	public ApplicationVersion getApplicationVersion() {
		return applicationVersion;
	}
	public void setApplicationVersion(ApplicationVersion version) {
		applicationVersion = version;
	}
	
	/**
	 * @return The date the current version was successfully deployed to the device
	 */
	@Column(name="deployment_date") @Temporal(TemporalType.TIMESTAMP)
	public Date getDeploymentDate() {
		return deploymentDate;
	}
	public void setDeploymentDate(Date date) {
		deploymentDate = date;
	}
	
	@Column(name="last_authentication") @Temporal(TemporalType.TIMESTAMP)
	public Date getLastAuthentication() {
		return authDate;
	}
	public void setLastAuthentication(Date authDate) {
		this.authDate = authDate;
	}
	
	public Map<Method,String> validate() {
		try {
			Map<Method,String> errors = new HashMap<Method,String>();
			if( this.getApplicationVersion()==null )
				errors.put( this.getClass().getMethod("getApplicationVersion"), "must be an installation of a version");
			if( errors.size()>0 )
				return errors;
			return null;
		} catch(NoSuchMethodException nsme) {
			throw new RuntimeException(nsme);
		}
	}
}
