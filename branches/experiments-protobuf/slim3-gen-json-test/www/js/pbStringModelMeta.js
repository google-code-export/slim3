var StringModelMeta = {
	def: {
		1: function(cin, v){ v.hello = cin.readString();},
		2: function(cin, v){ v.japanese = cin.readString();},
		3: function(cin, v){ v.key = cin.readString();},
		4: function(cin, v){ v.surrogate = cin.readString();},
	},
	createEmptyModel: function(){
		var m = {};
		m.hello = null;
		m.japanese = null;
		m.key = null;
		m.surrogate = null;
		return m;
	},
	readModel: function(text){
		return pbCommon.readModel(text, this.def, this.createEmptyModel);
	},
	readModels: function(text){
		return pbCommon.readModels(text, this.def, this.createEmptyModel);
	},
};
