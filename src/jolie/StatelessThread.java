/***************************************************************************
 *   Copyright (C) by Fabrizio Montesi                                     *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU Library General Public     *
 *   License along with this program; if not, write to the                 *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 *                                                                         *
 *   For details about the authors of this software, see the AUTHORS file. *
 ***************************************************************************/

package jolie;

import jolie.process.CorrelatedProcess;
import jolie.process.Process;

public class StatelessThread extends ExecutionThread
{
	private ExecutionThread parent;

	public StatelessThread( ExecutionThread parent, Process process )
	{
		super( process, parent );
		this.parent = parent;
	}
	
	public StatelessThread( ExecutionThread parent, Process process, CorrelatedProcess notifyObj )
	{
		super( process, parent, notifyObj );
		this.parent = parent;
	}
	
	public jolie.State state()
	{
		return parent.state();
	}
}