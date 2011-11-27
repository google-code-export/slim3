tests.push({
	path: "data/InverseModelListRefModel.bin",
	onload: function(){
		var model = InverseModelListRefModelMeta.readModel(this.responseText);
		appendTitle("InverseModelListRefModel");
		appendResult("key", "agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYjwwM", model.key);
		appendResult("intValue", 100, model.intValue);
		appendModelArrayResult("children", [{"intValue":1000,"key":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYkAwM","parent":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYjwwM"},{"intValue":2000,"key":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYkQwM","parent":"agpVbml0IFRlc3Rzch8LEhhJbnZlcnNlTW9kZWxMaXN0UmVmTW9kZWwYjwwM"}], model.children);
	}
});
