import spock.lang.Specification

import vk7is.mhconvert.MaidenheadConverter as MC

class ConverterSpec extends Specification {
	@spock.lang.Unroll
	void "test calculation of grid reference corners - #testGrid"() {
		given: 'the results from attempting a conversion'
			def results = MC.calculateGridCorner( testGrid )

		expect: 'the results are correct and accurate'
			results[0] == expectedLoc[0]
			results[1] == expectedLoc[1]

		where:
			testGrid | expectedLoc
			'AA'     | [-180.0,-90.0]
			'JJ'     | [   0.0,  0.0]
			'RR'     | [ 160.0, 80.0]
			'JJ55'   | [  10.0,  5.0]
			'JJ55ll' | [  10.9166666663,  5.4583333337]
	}

	@spock.lang.Unroll
	void "test conversion from grid references - #testGrid"() {
		given: 'the results from attempting a conversion'
			def results = MC.convertFrom( testGrid )

		expect: 'the results are correct and accurate'
			results[0] == expectedLoc[0]
			results[1] == expectedLoc[1]

		where:
			testGrid | expectedLoc
			'AA'     | [-170.0,-85.0]
			'RR'     | [ 170.0, 85.0]
			'JJ'     | [  10.0,  5.0]
			'AA00'   | [-179.0,-89.5]
			'JJ00'   | [   1.0,  0.5]
			'RR99'   | [ 179.0, 89.5]
			'AA00aa' | [-179.95833333335,-89.97916666665]
			'JJ00aa' | [   0.04166666665,  0.02083333335] // as close to zero as we can get with 6 chars
			'RR99xx' | [ 179.95833333255, 89.97916666745]
	}
	
	@spock.lang.Unroll
	void "test calculation of grid reference's polygon - #testGrid"() {
		given: 'the results from attempting a calculation'
			def results = MC.calculateGridPolygon( testGrid )

		expect: 'the polygon is correct'
			results == expectedPolygon

		where:
			testGrid | expectedPolygon
			'AA'     | [[-180,-90], [-180,-80], [-160,-80], [-160,-90]]
			'BB11'   | [[-158,-79], [-158,-78], [-156,-78], [-156,-79]]
			'KL56vw' | [[31.7499999993,26.9166666674], [31.7499999993,26.9583333341], [31.8333333326,26.9583333341], [31.8333333326,26.9166666674]]
	}

	@spock.lang.Unroll
	void 'test conversion from longitude and latitude to grid reference - #testLon, #testLat == #expectedGrid'() {
		given: 'the results from attempting a calculation'
			String result = MC.convertTo( testLon, testLat, requestedChars )

		expect: 'the calculated grid is correct'
			result == expectedGrid

		where:
			testLon | testLat | requestedChars | expectedGrid
			0       | 0       | 2              | 'JJ'
			0       | 0       | 4              | 'JJ00'
			0       | 0       | 6              | 'JJ00aa'
			180     | 90      | 2              | 'RR'
			180     | 90      | 4              | 'RR99'
			180     | 90      | 6              | 'RR99xx'
			-180    | -90     | 2              | 'AA'
			-180    | -90     | 4              | 'AA00'
			-180    | -90     | 6              | 'AA00aa'
			10      | 5       | 4              | 'JJ55'
			10      | 5       | 4              | 'JJ55'
			10.958  | 5.479   | 6              | 'JJ55ll'
	}

	void 'test invalid parameters are correctly handled by convertFrom'() {
		when: 'A call with an invalid grid ref'
			MC.convertFrom( badGrid )

		then:
			thrown IllegalArgumentException

		where:
			badGrid << ['', 'A', 'abc', 'ABC123ABC123']
	}
}
