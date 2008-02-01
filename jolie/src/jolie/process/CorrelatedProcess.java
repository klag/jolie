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

package jolie.process;

import jolie.Constants;
import jolie.ExecutionThread;
import jolie.Interpreter;
import jolie.StatefulThread;
import jolie.runtime.FaultException;

public class CorrelatedProcess implements Process
{
	private Process process;
	//private ExecutionThread waitingThread = null;
	private boolean waiting = false;
	
	public CorrelatedProcess( Process process )
	{
		this.process = process;
	}
	
	private void startSession()
	{
		waiting = true;
		new StatefulThread( process, ExecutionThread.currentThread(), this ).start();
		//waitingThread.start();
	}
	
	public void run()
		throws FaultException
	{
		if ( Interpreter.executionMode() != Constants.ExecutionMode.SINGLE ) {
			while( !Interpreter.exiting() ) {
				startSession();
				synchronized( this ) {
					if ( waiting ) { // We are still waiting for an input
						try {
							wait();
						} catch( InterruptedException ie ) {}
					}
				}
			}
		} else
			process.run();
	}
	
	public synchronized void inputReceived()
	{
		if ( Interpreter.executionMode() == Constants.ExecutionMode.CONCURRENT ) {
			waiting = false;
			notify();
		}
	}
	
	public synchronized void sessionTerminated()
	{
		if ( Interpreter.executionMode() == Constants.ExecutionMode.SEQUENTIAL ) {
			waiting = false;
			notify();
		}
	}
	
	public synchronized void signalFault( FaultException f )
	{
		Interpreter.logUnhandledFault( f );
		
		if ( Interpreter.executionMode() == Constants.ExecutionMode.SEQUENTIAL ) {
			waiting = false;
			notify();
		}
	}
}
