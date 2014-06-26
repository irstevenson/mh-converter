package vk7is.mhconvert

class CLI {
	static void main( String[] args ) {
		usage()
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
