package vk7is.mhconvert

import groovy.transform.Memoized

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
	@Memoized
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

	/**
	* Convert from a longitude and latitude to a grid reference of the specified number of
	* characters.
	*
	* @params lon longitude
	* @params lat latitude
	* @params numChars Even value specifying the numbers of characters (level of resolution) of the
	*                  returned grid reference.
	*/
	@Memoized
	static String convertTo( BigDecimal lon, BigDecimal lat, Integer numChars ) {
		if( lon < -180 || lon > 180 )
			throw new IllegalArgumentException( 'longitude needs to be between -180 and 180' )
		if( lat < -90 || lat > 90 )
			throw new IllegalArgumentException( 'latitude needs to be between -90 and 90' )
		if( numChars % 2 != 0 ) 
			throw new IllegalArgumentException( 'numChars must be an even number' )
		if( numChars < 2 || numChars > 6 )
			throw new IllegalArgumentException( 'numChars must be between two and six even characters' )

		BigDecimal workingLon = new BigDecimal(180 + lon).min(359.999999999) // need to cap the other extreme edge
		BigDecimal workingLat = new BigDecimal(90 + lat).min(179.999999999)

		String gridRef = (char)(((char)'A') + (workingLon/20))
		gridRef += (char)(((char)'A') + (workingLat/10))
		if( numChars > 2 ) {
			gridRef += (char)(((char)'0') + (workingLon.remainder( 20 ) / 2))
			gridRef += (char)(((char)'0') + (workingLat.remainder( 10 ) / 1))

			if( numChars > 4 ) {
				gridRef += (char)(((char)'a') + (workingLon.remainder( 20 ).remainder( 2 ) / DEG_50))
				gridRef += (char)(((char)'a') + (workingLat.remainder( 10 ).remainder( 1 ) / DEG_25))
			}
		}

		gridRef
	}

	/**
	* Converts from a Maidenhead Locator System (MLS) reference to a decimal longitude and latitude
	* reference of the designated squares lower left corner.
	*
	* @param gridRef a MLS reference up to six characters
	*
	* @return an array of two elements, the first being the longitude, the second being the latitude
	*/
	@Memoized
	static ArrayList<BigDecimal> calculateGridCorner( String gridRef ) {
		if( gridRef.length() % 2 != 0 ) 
			throw new IllegalArgumentException( 'gridRef must be an even number of characters' )
		if( gridRef.length() < 2 || gridRef.length() > 6 )
			throw new IllegalArgumentException( 'gridRef must be a length between two and six even characters' )

		// First, convert the String to an array of characters
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
	@Memoized
	static ArrayList< ArrayList<BigDecimal> > calculateGridPolygon( String gridRef ) {
		if( gridRef.length() % 2 != 0 ) 
			throw new IllegalArgumentException( 'gridRef must be an even number of characters' )
		if( gridRef.length() < 2 || gridRef.length() > 6 )
			throw new IllegalArgumentException( 'gridRef must be a length between two and six even characters' )

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
