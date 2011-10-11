tests.push({
	path: "data/DoubleModel.bin",
	onload: function(){
		var model = DoubleModelMeta.readModel(this.responseText);
		appendTitle("DoubleModel");
		appendResult("doubleValue", "3.2305409446133365E24", model.doubleValue);
		appendResult("negativeDoubleValue", "-233323.4333584", model.negativeDoubleValue);
		appendResult("nanValue", "NaN", model.nanValue);
		appendResult("infinityValue", "Infinity", model.infinityValue);
		appendResult("negativeInfinityValue", "-Infinity", model.negativeInfinityValue);
		appendResult("maxValue", "1.7976931348623157E308", model.maxValue);
		appendResult("minNormalValue", "2.2250738585072014E-308", model.minNormalValue);
		appendResult("minValue", "4.9E-324", model.minValue);
		appendResult("minM2Value", "1.0E-323", model.minM2Value);
	}
});
