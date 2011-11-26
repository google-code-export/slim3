tests.push({
	path: "data/ModelRefModel.bin",
	onload: function(){
		var model = ModelRefModelMeta.readModel(this.responseText);
		appendTitle("ModelRefModel");
		appendResult("key", "agpVbml0IFRlc3RzchQLEg1Nb2RlbFJlZk1vZGVsGJEDDA", model.key);
		appendNestedResult("modelRefValue", {"intValue":1000,"key":"agpVbml0IFRlc3RzchQLEg1Nb2RlbFJlZk1vZGVsGJIDDA"}, model.modelRefValue);
		appendResult("intValue", 100, model.intValue);
	}
});
