/*   
    Copyright (C) 2013 ApPeAL Group, Politecnico di Torino

    This file is part of TraCI4J.

    TraCI4J is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TraCI4J is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TraCI4J.  If not, see <http://www.gnu.org/licenses/>.
*/

package sim.traci4j.src.java.it.polito.appeal.traci;

import sim.traci4j.src.java.de.uniluebeck.itm.tcpip.Storage;
import sim.traci4j.src.java.it.polito.appeal.traci.protocol.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;

class CloseQuery extends ChangeStateQuery {

	CloseQuery(DataInputStream dis, DataOutputStream dos) {
		super(dis, dos, Constants.CMD_CLOSE);
	}

	@Override
	protected void writeRequestTo(Storage content) {
	}

}
