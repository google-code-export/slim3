tests.push({
	path: "data/InverseModelListRefModel.bin",
	onload: function(){
		var model = InverseModelListRefModelMeta.readModel(this.responseText);
		appendTitle("InverseModelListRefModel");
		appendResult("key", "agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYwwMM", model.key);
		appendResult("intValue", 100, model.intValue);
		appendModelArrayResult("children", [{"intValue":1000,"key":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYxAMM","parent":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYwwMM"},{"intValue":2000,"key":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYxQMM","parent":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYwwMM"}], model.children);
	}
});
