tests.push({
	path: "data/StringModel.bin",
	onload: function(){
		var model = StringModelMeta.readModel(this.responseText);
		appendTitle("StringModel");
		appendResult("hello", "hello", model.hello);
		appendResult("japanese", "こんにちは", model.japanese);
		appendResult("surrogate", "叱", model.surrogate);
	}
});
