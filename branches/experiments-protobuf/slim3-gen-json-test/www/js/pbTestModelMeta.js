var TestModelMeta = {
	def: {
		1: function(cin, v){ v.key = cin.readString();},
		2: function(cin, v){ v.name = cin.readString();},
	},
	readModel: function(text){
		return pbCommon.readModel(text, this.def);
	},
	readModels: function(text){
		return pbCommon.readModels(text, this.def);
	}
};
