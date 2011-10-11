tests.push({
	path: "data/IntModel.bin",
	onload: function(){
		var model = IntModelMeta.readModel(this.responseText);
		appendTitle("IntModel");
		appendResult("positive", "1048576", model.positive);
		appendResult("negative", "-13924443", model.negative);
		appendResult("max", "2147483647", model.max);
		appendResult("min", "-2147483648", model.min);
	}
});
