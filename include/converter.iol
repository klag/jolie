/***************************************************************************
 *   Copyright (C) 2015 by Matthias Dieter Wallnöfer                       *
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

include "types/IOException.iol"

type RawToStringRequest:raw {
	.charset?:string // set the encoding. Default: system (eg. for Unix-like OS UTF-8)
}

type StringToRawRequest:string {
	.charset?:string // set the encoding. Default: system (eg. for Unix-like OS UTF-8)
}

type ValueToXmlRequest: void {
	.value: undefined
	.doctype_system?:string // If format is "xml", adds it as a DOCTYPE system tag
	.schema*:string
	.xml_format?: bool
	.indent?:bool // if true, indentation is applied to file (default: false)
	.encoding?:string // set the encoding. Default: system (eg. for Unix-like OS UTF-8) or format's default (for XML and JSON UTF-8)
}

type XmlStringToValueRequest: void {
	.charset?: string
	.xml_format?: bool
	.xmlString: string
}

interface ConverterInterface {
RequestResponse:
	rawToBase64( raw )( string ),
	base64ToRaw( string )( raw ) throws IOException(IOExceptionType),

	/* string <-> raw (byte arrays) conversion methods */
	rawToString( RawToStringRequest )( string ) throws IOException(IOExceptionType),
	stringToRaw( StringToRawRequest )( raw ) throws IOException(IOExceptionType),
	valueToXml( ValueToXmlRequest )( string ) throws ConversionError( string ),
	xmlStringToValue( XmlStringToValueRequest )( undefined ) throws ConversionError( string )
}

outputPort Converter {
Interfaces: ConverterInterface
}

embedded {
Java:
	"joliex.util.Converter" in Converter
}
