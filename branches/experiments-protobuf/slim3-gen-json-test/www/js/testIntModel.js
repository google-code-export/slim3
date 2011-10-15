tests.push({
	path: "data/IntModel.bin",
	onload: function(){
		var model = IntModelMeta.readModel(this.responseText);
		appendTitle("IntModel");
		appendResult("max", "2147483647", model.max);
		appendResult("positive", "1048576", model.positive);
		appendResult("negative", "-12924443", model.negative);
		appendResult("min", "-2147483648", model.min);
	}
});
