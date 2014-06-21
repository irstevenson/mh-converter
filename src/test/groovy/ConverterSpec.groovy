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

	void 'test invalid parameters are correctly handled by convertFrom'() {
		when: 'A call with an invalid grid ref'
			MC.convertFrom( badGrid )

		then:
			thrown IllegalArgumentException

		where:
			badGrid << ['', 'A', 'abc', 'ABC123ABC123']
	}
}
