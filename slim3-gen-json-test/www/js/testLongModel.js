tests.push({
	path: "data/LongModel.bin",
	onload: function(){
		var model = LongModelMeta.readModel(this.responseText);
		appendTitle("LongModel");
		appendResult("max", 9223372036854775807, model.max);
		appendResult("positive", 32342423444, model.positive);
		appendResult("negative", -23948821142, model.negative);
		appendResult("min", -9223372036854775808, model.min);
		appendResult("minus1", -1, model.minus1);
		appendResult("v2p52", 4503599627370496, model.v2p52);
		appendResult("v2p53", 9007199254740992, model.v2p53);
		appendResult("v2p54", 18014398509481984, model.v2p54);
		appendResult("minusB20", -1035162, model.minusB20);
		appendResult("minusB36", -7794613171, model.minusB36);
		appendResult("almostMin1", -4096, model.almostMin1);
		appendResult("almostMin2", -9221120237041090561, model.almostMin2);
	}
});
