package vk7is.mhconvert

class MaidenheadConverter {
	private static BigDecimal DEG_25 = 2.5 / 60 // 2.5 degrees
	private static BigDecimal DEG_50 = 5.0 / 60 // 5 degrees

	/**
	* Converts from a Maidenhead Locator System (MLS) reference to a decimal longitude and latitude
	* reference repesention the designated squares center.
	*
	* @param gridRef a MLS reference up to six characters
	*
	* @return an array of two elements, the first being the longitude, the second being the latitude
	*/
	static ArrayList<BigDecimal> convertFrom( String gridRef ) {
		if( gridRef.length() % 2 != 0 ) 
			throw new IllegalArgumentException( 'gridRef must be an even number of characters' )

		if( gridRef.length() < 2 || gridRef.length() > 6 )
			throw new IllegalArgumentException( 'gridRef must be a length between two and six even characters' )

		def gridCorner = calculateGridCorner( gridRef )
		BigDecimal lon = gridCorner[0], lat = gridCorner[1]

		switch( gridRef.length() ) {
			case 2:
				// Center the co-ords to the square
				lon += 10
				lat += 5
				break
			case 4:
				// Center the co-ords to the square
				lon += 1
				lat += 0.5
				break
			case 6:
				// Center the co-ords to the square
				lon += DEG_50 / 2
				lat += DEG_25 / 2
				break
		}

		[lon,lat]
	}

	static String convertTo( BigDecimal lat, BigDecimal lon ) {
		// convert to a grid grid reference
	}

	/**
	* Converts from a Maidenhead Locator System (MLS) reference to a decimal longitude and latitude
	* reference of the designated squares lower left corner.
	*
	* @param gridRef a MLS reference up to six characters
	*
	* @return an array of two elements, the first being the longitude, the second being the latitude
	*/
	static ArrayList<BigDecimal> calculateGridCorner( String gridRef ) {
		// TODO: Add sanity check on gridRef - should this be done as a common method somehow?
		def gridRefChars = gridRef.toUpperCase().collect { (char)it }

		BigDecimal lon = (gridRefChars[0] - (char)'A') * 20 - 180
		BigDecimal lat = (gridRefChars[1] - (char)'A') * 10 - 90

		if( gridRefChars.size() >= 4 ) {
			lon += (gridRefChars[2] - (char)'0') * 2
			lat += (gridRefChars[3] - (char)'0')

			if( gridRefChars.size() >= 6 ) {
				lon += (gridRefChars[4] - (char)'A') * DEG_50
				lat += (gridRefChars[5] - (char)'A') * DEG_25
			}
		}

		[lon, lat]
	}

	/**
	* Provides an array with the four points (corners) of the grid square.
	* 
	* @param gridRef a two, four or six character grid reference
	*
	* @return an array of two element arrays. Each two element array is a longitude and latidude of
	*         that corners position. Each two element array is in order where a line being drawn
	*         from each one to the next (including from the last back to the first) will result in a
	*         square represening the gridRef square.
	*/
	static ArrayList< ArrayList<BigDecimal> > calculateGridPolygon( String gridRef ) {
		// TODO: Add sanity check on gridRef - should this be done as a common method somehow?
		ArrayList<BigDecimal> p1, p2, p3, p4

		p1 = calculateGridCorner( gridRef )

		def latInc, lonInc
		switch( gridRef.length() ) {
			case 2:
				latInc = 10
				lonInc = 20
				break
			case 4:
				latInc = 1
				lonInc = 2
				break
			case 6:
				latInc = DEG_25
				lonInc = DEG_50
				break
		}

		p2 = [p1[0],          p1[1] + latInc]
		p3 = [p1[0] + lonInc, p1[1] + latInc]
		p4 = [p1[0] + lonInc, p1[1]         ]

		[p1, p2, p3, p4]
	}
}