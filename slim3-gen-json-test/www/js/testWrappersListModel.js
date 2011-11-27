tests.push({
	path: "data/WrappersListModel.bin",
	onload: function(){
		var model = WrappersListModelMeta.readModel(this.responseText);
		appendTitle("WrappersListModel");
		appendResult("booleanList", new Array(true, false, true), model.booleanList);
		appendResult("doubleList", new Array(1.1, 2.2, 3.3), model.doubleList);
		appendResult("floatList", new Array(1.1, 2.2, 3.3), model.floatList);
		appendResult("integerList", new Array(1, 2, 3), model.integerList);
		appendResult("longList", new Array(1, 2, 3), model.longList);
		appendResult("shortList", new Array(1, 2, 3), model.shortList);
	}
});
