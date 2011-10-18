tests.push({
	path: "data/DoubleModel.bin",
	onload: function(){
		var model = DoubleModelMeta.readModel(this.responseText);
		appendTitle("DoubleModel");
		appendResult("infinityValue", Infinity, model.infinityValue);
		appendResult("maxValue", 1.7976931348623157e+308, model.maxValue);
		appendResult("minNormalValue", 2.2250738585072014e-308, model.minNormalValue);
		appendResult("minValue", 4.9e-324, model.minValue);
		appendResult("nanValue", NaN, model.nanValue);
		appendResult("negativeInfinityValue", -Infinity, model.negativeInfinityValue);
		appendResult("doubleValue", 72057594037927936., model.doubleValue);
		appendResult("negativeDoubleValue", -233323.4333584, model.negativeDoubleValue);
		appendResult("minM2Value", 1.e-323, model.minM2Value);
	}
});
