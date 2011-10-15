tests.push({
	path: "data/DoubleModel.bin",
	onload: function(){
		var model = DoubleModelMeta.readModel(this.responseText);
		appendTitle("DoubleModel");
		appendResult("infinityValue", "Infinity", model.infinityValue);
		appendResult("maxValue", "1.7976931348623157E308", model.maxValue);
		appendResult("minNormalValue", "2.2250738585072014E-308", model.minNormalValue);
		appendResult("minValue", "4.9E-324", model.minValue);
		appendResult("nanValue", "NaN", model.nanValue);
		appendResult("negativeInfinityValue", "-Infinity", model.negativeInfinityValue);
		appendResult("doubleValue", "7.2057594037927936E16", model.doubleValue);
		appendResult("negativeDoubleValue", "-233323.4333584", model.negativeDoubleValue);
		appendResult("minM2Value", "1.0E-323", model.minM2Value);
	}
});
