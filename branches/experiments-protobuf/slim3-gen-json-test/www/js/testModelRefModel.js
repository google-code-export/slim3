tests.push({
	path: "data/ModelRefModel.bin",
	onload: function(){
		var model = ModelRefModelMeta.readModel(this.responseText);
		appendTitle("ModelRefModel");
		appendResult("key", "agpVbml0IFRlc3RzchQLEg1Nb2RlbFJlZk1vZGVsGN0LDA", model.key);
		appendNestedResult("modelRefValue", {"intValue":1000,"key":"agpVbml0IFRlc3RzchQLEg1Nb2RlbFJlZk1vZGVsGN4LDA"}, model.modelRefValue);
		appendResult("intValue", 100, model.intValue);
	}
});
