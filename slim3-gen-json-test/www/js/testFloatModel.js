tests.push({
	path: "data/FloatModel.bin",
	onload: function(){
		var model = FloatModelMeta.readModel(this.responseText);
		appendTitle("FloatModel");
		appendResult("positive", "3443.822265625", model.positive);
		appendResult("infinityValue", "Infinity", model.infinityValue);
		appendResult("maxValue", "3.4028234663852886e+38", model.maxValue);
		appendResult("minNormalValue", "1.1754943508222875e-38", model.minNormalValue);
		appendResult("minValue", "1.401298464324817e-45", model.minValue);
		appendResult("nanValue", "NaN", model.nanValue);
		appendResult("negativeFloatValue", "-233323.4375", model.negativeFloatValue);
		appendResult("negativeInfinityValue", "-Infinity", model.negativeInfinityValue);
		appendResult("minP1Value", "2.802596928649634e-45", model.minP1Value);
	}
});
