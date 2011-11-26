tests.push({
	path: "data/AnnotatedStringModel.bin",
	onload: function(){
		var model = AnnotatedStringModelMeta.readModel(this.responseText);
		appendTitle("AnnotatedStringModel");
		appendResult("hello", "Hello, World.", model.hello);
		appendResult("aliased", "world", model.aliased);
	}
});
