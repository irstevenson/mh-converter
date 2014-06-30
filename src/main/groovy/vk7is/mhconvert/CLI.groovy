package vk7is.mhconvert

class CLI {
	static void main( String[] args ) {
		if( args.size() < 2 ) {
			println 'Incorrect usage...'
			usage()
			System.exit( 1 )
		}

		try {
			switch( args[0] ) {
				case 'fromGrid':
					def coords = MaidenheadConverter.convertFrom( args[1] )
					println "Grid Reference: ${args[1]}"
					println "Longitude:      ${coords[0]}"
					println "Latitude:       ${coords[1]}"

					break

				case 'toGrid':
					if( args.size() != 4 )
						throw new IllegalArgumentException( 'toGrid requires 3 parameters' )

					def coords = [longitude: args[1].toBigDecimal(), latitude: args[2].toBigDecimal()]
					def numChars = args[3].toInteger()
					def gridRef = MaidenheadConverter.convertTo( coords.longitude, coords.latitude, numChars )
					println "${numChars} character grid reference for:"
					println "Longitude : ${coords.longitude}"
					println "Latitude  : ${coords.latitude}"
					println 'Grid Reference:'
					println "  ${gridRef}"

					break

				default:
					throw new IllegalArgumentException( 'Unrecognised command...' )
			}
		}
		catch( IllegalArgumentException iae ) {
			System.err.println "Unable to process command: ${iae.message}"
			usage()
			System.exit( 1 )
		}
	}

	static usage() {
		println '''\
Usage:
java -jar mhconvert.jar <command> <options>

Where:
command     one of fromGrid or toGrid

fromGrid <gridRef>
Where:
  gridRef   a 2, 4 or 6 character grid reference

  example:
  java -jar mhconvert.jar fromGrid QE56

toGrid <longitude> <latitude> <refLength>
Where:
  lon       longitude of requested grid reference
  lat       latitude of requested grid reference
  refLength the number of characters of the return reference
            (must be 2, 4 or 6)

  example
  java -jar mhconvert.jar toGrid 147.6 -42.8 6
    would return
  QE37te
'''
	}
}
